FROM openjdk:17-jdk-slim

# Copia o JAR da aplicação
COPY target/werp-backend-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta 3000 (informativo)
EXPOSE 3000

# Comando para rodar a aplicação standalone
ENTRYPOINT ["java","-jar","/app.jar"]