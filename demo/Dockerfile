# Usa Java 17
FROM eclipse-temurin:17-jdk-alpine

# Carpeta dentro del container
WORKDIR /app

# Copia el JAR compilado
COPY target/*.jar app.jar

# Exponer el puerto que Spring Boot escucha
EXPOSE 8080

# Arranca Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
