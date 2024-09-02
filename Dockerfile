FROM node:latest as build-frontend

WORKDIR /app

COPY frontend/package*.json ./

RUN npm install

COPY frontend ./

RUN npm run build

FROM gradle:latest as build-backend

WORKDIR /home/gradle/project

COPY backend .

COPY --from=build-frontend /app/dist /home/gradle/project/src/main/resources/static

RUN gradle clean bootJar

FROM anchore/syft:latest as sbom-generator
COPY --from=build-backend /home/gradle/project/build/libs/*.jar /app.jar
RUN syft /app.jar -o json > /sbom.json

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /home/spring

COPY --from=build-backend /home/gradle/project/build/libs/*.jar /home/spring/app.jar
COPY --from=sbom-generator /sbom.json /home/spring/sbom.json

RUN chown -R spring:spring /home/spring

USER spring:spring

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["java", "-jar", "/home/spring/app.jar"]
