# 🚀 Microservicios Spring Boot + DynamoDB - Franquicias

Proyecto Java Spring Boot con AWS DynamoDB para manejar:

✅ Franquicias  
✅ Sucursales  
✅ Productos con stock

Infra administrada con Terraform.  
Listo para CI/CD.

---

## ⚙ Cómo ejecutar

### 🚀 Terraform
```bash
cd terraform
terraform init
terraform apply
```
Esto crea en AWS:
- Tabla `franquicia` (PAY_PER_REQUEST)
- Tabla `counters` para autoincrementos

---

### 🚀 Spring Boot
```bash
mvn clean install
mvn spring-boot:run
```
Por defecto se expone en:
```
http://localhost:8080
```

---

## 🚀 Endpoints disponibles

### ✅ Crear Franquicia
```bash
curl -X POST http://localhost:8080/franquicia \
-H "Content-Type: application/json" \
-d '{"nombre": "Franquicia A"}'
```

---

### ✅ Agregar Sucursales a Franquicia
```bash
curl -X POST http://localhost:8080/franquicia/sucursal \
-H "Content-Type: application/json" \
-d '{
  "nombreFranquicia": "Franquicia A",
  "sucursales": [
    {"nombre": "Sucursal Centro"},
    {"nombre": "Sucursal Norte"}
  ]
}'
```

---

### ✅ Agregar Productos a Sucursal
```bash
curl -X POST http://localhost:8080/franquicia/sucursal/producto \
-H "Content-Type: application/json" \
-d '{
  "nombreFranquicia": "Franquicia A",
  "nombreSucursal": "Sucursal Centro",
  "productos": [
    {"nombre": "Producto X", "cantidad": 150},
    {"nombre": "Producto Y", "cantidad": 90}
  ]
}'
```

---

### ✅ Eliminar Producto de una Sucursal
```bash
curl -X DELETE http://localhost:8080/franquicia/sucursal/producto/delete \
-H "Content-Type: application/json" \
-d '{
  "nombreFranquicia": "Franquicia A",
  "nombreSucursal": "Sucursal Centro",
  "nombreProducto": "Producto X"
}'
```

---

### ✅ Actualizar Nombre Franquicia
```bash
curl -X PATCH http://localhost:8080/franquicia/nombre \
-H "Content-Type: application/json" \
-d '{
  "idFranquicia": 1,
  "nombre": "Franquicia Renombrada"
}'
```

---

### ✅ Actualizar Nombre Sucursal
```bash
curl -X PATCH http://localhost:8080/franquicia/sucursal/nombre \
-H "Content-Type: application/json" \
-d '{
  "idSucursal": 2,
  "nombre": "Sucursal Renombrada"
}'
```

---

### ✅ Actualizar Nombre Producto
```bash
curl -X PATCH http://localhost:8080/franquicia/sucursal/producto/nombre \
-H "Content-Type: application/json" \
-d '{
  "idProducto": 3,
  "nombre": "Producto Premium"
}'
```

---

### ✅ Actualizar Stock Producto
```bash
curl -X PATCH http://localhost:8080/franquicia/sucursal/producto/cantidad \
-H "Content-Type: application/json" \
-d '{
  "idProducto": 3,
  "cantidad": 200
}'
```

---

### ✅ Ver el producto con más stock por sucursal
Retorna un listado con el producto con más stock por cada sucursal de una franquicia.

> ⚠️ **Nota:** ignora productos que no tengan cantidad (`null`) para evitar errores.

```bash
curl -X GET "http://localhost:8080/franquicia/max-stock?nombreFranquicia=Franquicia A"
```

Ejemplo de respuesta:
```json
[
  {
    "sucursal": "Sucursal Centro",
    "producto": "Producto X",
    "cantidad": 150
  },
  {
    "sucursal": "Sucursal Norte",
    "producto": "Producto Y",
    "cantidad": 90
  }
]
```

---

## ⚠️ Errores controlados
Si un recurso no existe, retorna `404` con un JSON así:

```json
{
  "error": "Producto con nombre 'Producto X' no encontrado en sucursal 'Sucursal Centro'"
}
```

o

```json
{
  "error": "Franquicia con ID 5 no encontrada"
}
```

---

## ✅ Stack
- Java 17
- Spring Boot 3.1
- Lombok
- Gson
- AWS SDK v2 DynamoDB
- Terraform

---
# 🚀 📦 Docker & Docker Compose para Spring Boot + DynamoDB

Este proyecto empaqueta el microservicio Spring Boot en un contenedor Docker optimizado con Java 17, listo para ejecutarse en cualquier máquina con Docker y conectarse a AWS DynamoDB.

---

## ✅ Requisitos previos

Verifica que tengas Docker y WSL correctamente instalados y actualizados:

```bash
docker --version
wsl --update
```

---

## 🚀 📂 Dockerfile

El `Dockerfile` se encuentra en la raíz del proyecto. Este archivo construye una imagen con Java 17 y tu aplicación Spring Boot.

```dockerfile
# Usa una imagen base optimizada para Java 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio dentro del contenedor
WORKDIR /app

# Copia el jar generado
COPY target/*.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando para arrancar Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🚀 📂 docker-compose.yml

También se incluye un `docker-compose.yml` para simplificar el despliegue local con variables de entorno para las credenciales AWS.

```yaml
version: "3.8"
services:
  app:
    image: springboot-dynamodb
    ports:
      - "8080:8080"
    environment:
      AWS_ACCESS_KEY_ID: 
      AWS_SECRET_ACCESS_KEY: 
      AWS_REGION: us-east-1
```

---

## 🚀 🔥 Pasos para construir y ejecutar

### 1️⃣ Construir el jar de Spring Boot

```bash
mvn clean package
```

Esto generará en `target/` un archivo similar a:

```
target/springboot-dynamodb-microservices-1.0.0.jar
```

---

### 2️⃣ Construir la imagen Docker

```bash
docker build -t springboot-dynamodb .
```

---

### 3️⃣ Ejecutar el contenedor Docker localmente

```bash
docker run -p 8080:8080 springboot-dynamodb
```

---

### 4️⃣ Ejecutar el contenedor pasando las credenciales AWS (para DynamoDB)

```bash
docker run -p 8080:8080 -e AWS_ACCESS_KEY_ID= -e AWS_SECRET_ACCESS_KEY= -e AWS_REGION=us-east-1   springboot-dynamodb
```

---

### 5️⃣ Ejecutar usando Docker Compose

Con el `docker-compose.yml`, puedes levantar el servicio directamente con:

```bash
docker-compose up
```

🚀 **Listo para producción y CI/CD**

## 🚀 📦 Build, tag y push del microservicio Java Spring Boot a AWS ECR

```bash
# 1. Compilar el proyecto Java
mvn clean package

# 2. Construir la imagen Docker
docker build -t springboot-dynamodb .

# 3. Login en AWS ECR
aws ecr get-login-password --region us-east-1 | \
docker login --username AWS --password-stdin ${idcuenta}.dkr.ecr.us-east-1.amazonaws.com

# 4. Taggear la imagen para ECR
docker tag springboot-dynamodb:latest ${idcuenta}$.dkr.ecr.us-east-1.amazonaws.com/springboot-dynamodb:latest

# 5. Push a ECR
docker push ${idcuenta}.dkr.ecr.us-east-1.amazonaws.com/springboot-dynamodb:latest