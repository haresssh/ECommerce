# E-Commerce Microservices Application

This is a microservices-based E-Commerce application built with Spring Boot, Spring Cloud, and Docker. It consists of several services that handle different aspects of the e-commerce domain, such as user management, product catalog, order processing, and inventory management.

## Services

| Service | Port | Description | Swagger UI |
| :--- | :--- | :--- | :--- |
| **User Service** | 8080 | Manages user registration, authentication, and profiles. | [Link](http://localhost:8080/swagger-ui.html) |
| **Product Service** | 8081 | Manages product catalog, categories, and product details. | [Link](http://localhost:8081/swagger-ui.html) |
| **Notification Service** | 8082 | Handles notifications (e.g., email, SMS) - *In Development*. | N/A |
| **Order Service** | 8083 | Manages order placement, status updates, and history. | [Link](http://localhost:8083/swagger-ui.html) |
| **Inventory Service** | 8084 | Manages product stock levels and reservations. | [Link](http://localhost:8084/swagger-ui.html) |

## Infrastructure

The application uses the following infrastructure components:

*   **MySQL**: Relational database for persistent storage.
*   **Redis**: In-memory data store for caching.
*   **Kafka**: Message broker for asynchronous communication between services.
*   **Zookeeper**: Coordination service for Kafka.

## Prerequisites

*   **Java 17**
*   **Maven**
*   **Docker** & **Docker Compose**

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/haresssh/ECommerce.git
cd ECommerce
```

### 2. Build the Services

You can build the services using Maven or Docker Compose.

**Using Docker Compose (Recommended):**

```bash
docker-compose build
```

**Using Maven:**

```bash
mvn clean package -DskipTests
```

### 3. Run the Application

Start the entire microservices ecosystem using Docker Compose:

```bash
docker-compose up -d
```

This command will start all the microservices and the required infrastructure containers (MySQL, Redis, Kafka, Zookeeper).

### 4. Verify the Deployment

Check if the containers are running:

```bash
docker ps
```

You can also check the logs of a specific service:

```bash
docker logs -f <service_name>
# Example: docker logs -f userservice
```

## API Documentation

The application uses **SpringDoc OpenAPI** (Swagger) for API documentation. Once the services are running, you can access the interactive API documentation at the following URLs:

*   **User Service**: http://localhost:8080/swagger-ui.html
*   **Product Service**: http://localhost:8081/swagger-ui.html
*   **Order Service**: http://localhost:8083/swagger-ui.html
*   **Inventory Service**: http://localhost:8084/swagger-ui.html

## Testing

A Postman collection is included in the repository (`ECommerce.postman_collection.json`) for End-to-End (E2E) testing. You can import this collection into Postman to test the various API endpoints.

## Project Structure

```
ECommerce/
├── userservice/          # User management service
├── productservice/       # Product catalog service
├── orderservice/         # Order processing service
├── inventoryservice/     # Inventory management service
├── notificationservice/  # Notification service
├── init-scripts/         # Database initialization scripts
├── docker-compose.yml    # Docker Compose configuration
├── ECommerce.postman_collection.json # Postman tests
└── README.md             # Project documentation
```
