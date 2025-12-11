# Breaking Bad Backend Frases SpringBoot-BigQuery-CloudRun 

Este proyecto es un ejemplo experimental de como transformar un backend sencillo para:
- Usar **Spring Boot 3** y **Java 17**
- Consultar datos desde **BigQuery** (en lugar de una base de datos relacional tradicional)
- Seguir una arquitectura mas "empresarial": **Controlador-Servicio-Repositorio**
- Empaquetar la aplicacion en Docker
- Dejarla lista para desplegar en **Cloud Run**

La idea es que este ejemplo sirva como **ejemplo/experimento** para migrar otros microservicios,
por ejemplo, de Oracle/WebLogic hacia Google Cloud Platform.

---

## 1. Arquitectura del codigo

Capa por capa:

- **Controlador (`FraseController`)**
  - Expone el endpoint REST `/api/frases/aleatoria`
  - Recibe la peticion HTTP y delega al servicio

- **Servicio (`FraseService`)**
  - Contiene la logica de negocio
  - Llama al repositorio para obtener datos
  - Puede aplicar validaciones o reglas de negocio

- **Repositorio (`FraseBigQueryRepository`)**
  - Se encarga de comunicarse directamente con BigQuery
  - Ejecuta las consultas SQL
  - Convierte las filas devueltas por BigQuery en objetos `Frase`

- **Modelo (`Frase`)**
  - Clase simple que representa una frase con `id` y `texto`

Esto hace que el codigo sea mas facil de mantener, probar y extender.

---

## 2. Dependencias principales (`pom.xml`)

- `spring-boot-starter-web`: para crear APIs REST
- `spring-boot-starter-validation`: para validaciones (opcional en este ejemplo)
- `google-cloud-bigquery:2.40.0`: cliente oficial de BigQuery en java


La version de BigQuery es **explicita** para evitar problemas con repositorios adicionales.

---

## 3. Configuracion por perfiles

### 3.1 `application.properties`

Indica el perfil activo por defecto:

```properties
spring.application.name=breakingbad-backend
spring.profiles.active=local
```

### 3.2 `application-local.yaml`

Se usa cuando corre la aplicacion local:

- Puerto 8080
- Datos de BigQuery para desarrollo

### 3.3 `application-gcp.yaml`

Se usa cuando la app se despliega en Cloud Run:

- El puerto se toma de la variable de entorno `PORT`
- Los datos de BigQuery (dataset, tabla, proyecto) se leen de variables de entorno:
  - `PROJECT_ID`
  - `BQ_DATASET`
  - `BQ_TABLE`

---

## 4. Flujo de una peticion

1. Un cliente hace una peticion GET a:
   - `/api/frases/aleatoria`
2. `FraseController` recibe la peticion y llama a `FraseService`
3. `FraseService` llama a `FraseBigQueryRepository`
4. `FraseBigQueryRepository` ejecuta una consulta SQL en BigQuery:
   - `SELECT id, texto FROM tabla ORDER BY RAND() LIMIT 1`
5. Se crea un objeto `Frase` con los datos devueltos
6. El controlador devuelve ese objeto y spring lo convierte a JSON

---

## 5. Requisitos para ejecutar el proyecto

### 5.1 Software

- Java 17 instalado
- Maven 3.x
- Cuenta de Google Cloud (GCP)
- BigQuery habilitado en el proyecto
- gcloud CLI (opcional pero recomendable)
- Docker (para contenedores y Cloud Run)

### 5.2 Configuracion de BigQuery

1. Crear un **dataset**, por ejemplo: `breakingbad_dataset`
2. Crear una **tabla**, por ejemplo: `frases`

Ejemplo de tabla:

```sql
CREATE TABLE `tu-proyecto.breakingbad_dataset.frases` (
  id INT64,
  texto STRING
);
```

Insertar algunos datos:

```sql
INSERT INTO `tu-proyecto.breakingbad_dataset.frases` (id, texto) VALUES
  (1, "Autor - Frase"),
  (2, "Autor2 - Frase2"),
  (3, "Autor3 - Frase3");
```

---

## 6. Autenticacion con BigQuery en local

La forma sencilla es usar **Application Default Credentials**:

```bash
gcloud auth application-default login
```

Esto abre el navegador, te autenticas con la cuenta de Google y listo.

---

## 7. Ejecutar la aplicacion en local

### 7.1 Compilar

```bash
mvn clean package -DskipTests
```

### 7.2 Ejecutar

```bash
mvn spring-boot:run
```

o

```bash
java -jar target/breakingbad-backend-1.0.0.jar
```

### 7.3 Probar el endpoint

```bash
curl http://localhost:8080/api/frases/aleatoria
```

Respuesta esperada (ejemplo):

```json
{
  "id": 1,
  "texto": "Autor - Frase"
}
```

---

## 8. Docker y despliegue en Cloud Run

### 8.1 Construir el JAR

```bash
mvn clean package -DskipTests
```

### 8.2 Construir la imagen Docker

```bash
docker build -t breakingbad-backend:latest .
```

### 8.3 Probar local con Docker

```bash
docker run -p 8080:8080   -e PROJECT_ID=tu-proyecto   -e BQ_DATASET=breakingbad_dataset   -e BQ_TABLE=frases   breakingbad-backend:latest
```

---

## 9. Subir la imagen a Artifact Registry (ejemplo)

1. Crear repositorio:

```bash
gcloud artifacts repositories create backend-repo   --repository-format=docker   --location=us-central1   --description="Repositorio de imagenes Docker para backend Breaking Bad"
```

2. Etiquetar la imagen:

```bash
docker tag breakingbad-backend:latest   us-central1-docker.pkg.dev/TU_PROYECTO/backend-repo/breakingbad-backend:latest
```

3. Subirla:

```bash
docker push us-central1-docker.pkg.dev/TU_PROYECTO/backend-repo/breakingbad-backend:latest
```

---

## 10. Desplegar en Cloud Run

```bash
gcloud run deploy breakingbad-backend   --image=us-central1-docker.pkg.dev/TU_PROYECTO/backend-repo/breakingbad-backend:latest   --region=us-central1   --platform=managed   --allow-unauthenticated   --set-env-vars=SPRING_PROFILES_ACTIVE=gcp   --set-env-vars=PROJECT_ID=TU_PROYECTO,BQ_DATASET=breakingbad_dataset,BQ_TABLE=frases
```

---


