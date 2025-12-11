
# imagen Java 17 y una distribución de openJDK
FROM eclipse-temurin:17-jdk-alpine

# directorio de trabajo dentro del contenedor
WORKDIR /app
# copiamos el JAR generado por maven desde la carpeta target al directorio de trabajo
COPY target/*.jar app.jar
# exponemos el puerto 8080 dentro del contenedor
EXPOSE 8080
# variable de entorno para opciones adicionales de java (buenas practicas)
ENV JAVA_OPTS=""

# comando de arranque, ejecuta la aplicación spring boot
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
