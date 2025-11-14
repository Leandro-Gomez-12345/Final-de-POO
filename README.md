# Sistema de Gestión de Carrito de Compras - API REST

Proyecto final de Programación Orientada a Objetos (POO) - Sistema de carrito de compras desarrollado con Spring Boot, implementando una arquitectura hexagonal y exponiendo endpoints REST.

## Equipo de Desarrollo

- **Jose:** Configuración base, Entities y Repositories JPA
- **Leandro:** Services, UseCases y manejo de excepciones
- **Emmanuel:** Controllers REST y colección de Postman

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Data JPA**
- **MySQL 8**
- **Maven**
- **Postman** (testing de API)

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- Java 17 o superior
- MySQL 8 o superior
- Maven (integrado en IntelliJ IDEA)
- Postman (para probar los endpoints)

## Configuración de la Base de Datos

### 1. Crear la base de datos en MySQL
```sql
CREATE DATABASE carrito_db;
```

### 2. Configuración en `application.properties`

El archivo ya está configurado con las siguientes credenciales:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/carrito_db
spring.datasource.username=root
spring.datasource.password=root
```

Si tus credenciales de MySQL son diferentes, modifica el archivo `src/main/resources/application.properties`.

## Cómo Ejecutar el Proyecto

### Desde IntelliJ IDEA:

1. Abre el proyecto en IntelliJ IDEA
2. Espera a que Maven descargue todas las dependencias
3. Localiza la clase `ShoppingCartApplication.java`
4. Click derecho → **Run 'ShoppingCartApplication.main()'**
5. La aplicación se ejecutará en: **http://localhost:8080**

### Desde la terminal:
```
mvn clean install
mvn spring-boot:run
```

### Mensaje de éxito:
```
API REST del Carrito de Compras
Servidor corriendo en: http://localhost:8080
```

## Endpoints Disponibles

### Productos (CRUD)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/products` | Obtener todos los productos |
| GET | `/api/products/{id}` | Obtener producto por ID |
| POST | `/api/products` | Crear un nuevo producto |
| PUT | `/api/products/{id}` | Actualizar un producto |
| DELETE | `/api/products/{id}` | Eliminar un producto |

### Consultas de Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/products/category/{category}` | Productos por categoría |
| GET | `/api/products/price/greater-than?minPrice=X` | Productos con precio mayor a X |
| GET | `/api/products/price/range?minPrice=X&maxPrice=Y` | Productos en rango de precios |

### Carrito de Compras

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/cart` | Ver el carrito actual |
| POST | `/api/cart/add` | Agregar producto al carrito |
| PUT | `/api/cart/update` | Actualizar cantidad de un producto |
| DELETE | `/api/cart/remove/{productId}` | Eliminar producto del carrito |
| DELETE | `/api/cart/clear` | Vaciar el carrito completamente |

### Pedidos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/orders/checkout` | Cerrar pedido y aplicar descuentos |
| DELETE | `/api/orders/cancel` | Cancelar pedido (vaciar carrito) |

## Probar con Postman

### Importar la colección:

1. Abre Postman
2. Click en **"Import"**
3. Selecciona el archivo: `Shopping_Cart_API.postman_collection.json`
4. La colección contiene las **15 peticiones** organizadas y listas para usar

### Flujo de prueba recomendado:

1. **Ver productos:** `GET /api/products`
2. **Agregar al carrito:** `POST /api/cart/add`
```json
   {
       "productId": 2,
       "quantity": 3
   }
```
3. **Ver carrito:** `GET /api/cart`
4. **Cerrar pedido:** `POST /api/orders/checkout`

## Regla de Descuento

El sistema aplica automáticamente un **descuento del 5%** cuando el total del pedido es **mayor a $100,000**.

### Ejemplo:
```json
{
    "subtotal": 3250000.0,
    "discount": 162500.0,
    "finalTotal": 3087500.0
}
```

## Arquitectura del Proyecto
```
co.com.poo.shoppingcart
├── controllers/          # Endpoints REST (API)
├── services/            # Lógica de negocio y persistencia
├── usecase/             # Casos de uso del dominio
├── entities/            # Entidades JPA (base de datos)
├── dto/                 # Data Transfer Objects
│   ├── request/         # DTOs de entrada
│   └── response/        # DTOs de salida
├── exception/           # Manejo de excepciones
├── repositories/        # Repositorios JPA
└── model/              # Modelos del dominio
```

## Características Implementadas

✅ CRUD completo de productos  
✅ Consultas avanzadas (categoría, precio)  
✅ Gestión completa del carrito  
✅ Sistema de descuentos automático  
✅ Validaciones de entrada con `@Valid`  
✅ Manejo global de excepciones con `@RestControllerAdvice`  
✅ Persistencia en MySQL con JPA  
✅ Arquitectura hexagonal (Controllers → Services → Repositories)  
✅ DTOs para separar capa de presentación

## Estructura de Base de Datos

El sistema crea automáticamente las siguientes tablas:

- **products:** Catálogo de productos
- **carts:** Carritos de compra
- **cart_items:** Items dentro del carrito
- **orders:** Órdenes/pedidos cerrados

## Solución de Problemas

### Error: "Unknown database 'carrito_db'"
- Verifica que creaste la base de datos en MySQL
- Ejecuta: `CREATE DATABASE carrito_db;`

### Error: "Access denied for user 'root'"
- Verifica las credenciales en `application.properties`
- Asegúrate que MySQL esté corriendo

### Error: "Port 8080 already in use"
- Cierra otras aplicaciones que usen el puerto 8080
- O cambia el puerto en `application.properties`:
```properties
  server.port=8081
```

## Notas Adicionales

- La aplicación maneja un **único carrito** (ID = 1) por simplicidad
- El stock de productos **NO se gestiona** según los requisitos del proyecto
- Los productos iniciales se cargan desde el archivo `data.sql`
- El carrito se **vacía automáticamente** al cerrar un pedido

## Cumplimiento de Requisitos

Arquitectura hexagonal implementada  
API RESTful completa y funcional  
Persistencia en MySQL  
CRUD de productos  
Consultas por categoría y precio  
Gestión del carrito  
Descuento del 5% si total > $100,000  
Validaciones completas  
Manejo de excepciones con `@RestControllerAdvice`  
Colección de Postman incluida  
Código comentado y bien estructurado

## Contacto

Para dudas o consultas sobre el proyecto, contactar al equipo de desarrollo.

---

**Desarrollado con el corazón para la materia Programación Orientada a Objetos**