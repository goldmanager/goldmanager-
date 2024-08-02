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


FROM eclipse-temurin:21-jre-alpine


COPY --from=build-backend /home/gradle/project/build/libs/*.jar /app.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "/app.jar"]
