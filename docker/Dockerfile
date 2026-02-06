FROM eclipse-temurin:17-jre
WORKDIR /app

# build context 是 ./app，所以 jar 文件应在 ./app 目录下
COPY fileCopy.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
