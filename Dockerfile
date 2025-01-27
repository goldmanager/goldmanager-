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

RUN gradle clean bootJar -PskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /opt/goldmanager

COPY --from=build-backend /home/gradle/project/build/libs/*.jar /opt/goldmanager/app.jar

COPY --from=build-backend /home/gradle/project/build/reports/application.cdx.json /bom/application.cdx.json

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["java", "-jar", "/opt/goldmanager/app.jar"]
