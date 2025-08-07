# 🛒 Spring E‑Commerce Microservices

## 📘 Overview

This repository contains a small **e-commerce backend** split into multiple **Spring Boot microservices**.

It demonstrates:

- Service discovery via **Eureka**
- Centralized configuration with **Spring Cloud Config**
- Synchronous HTTP communication between services
- Asynchronous messaging via **Kafka**
- Persistence using **PostgreSQL** and **MongoDB**

---

## 🧱 Modules

| Module        | Description                                                                 |
|---------------|-----------------------------------------------------------------------------|
| `configuration` | Spring Cloud Config Server exposing service configs (native mode)          |
| `discovery`     | Eureka Server for service discovery                                        |
| `customer`      | Customer management service (**MongoDB**)                                  |
| `product`       | Product catalog and purchase service (**PostgreSQL**)                      |
| `order`         | Order management service (**PostgreSQL**) with Kafka producer integration  |

---

## 🗺️ High-Level Architecture

```plaintext
  [client]
     |
     v
  Order Service  ----HTTP---->  Product Service (PostgreSQL)
     |   \
     |    ----HTTP---->  Customer Service (MongoDB)
     | 
     |--(Kafka)--> order-topic (Order Confirmations)
     |
  Eureka (Discovery) <--- All services register
     |
  Config Server (8888) <--- All services fetch configs from here
```
---

## ⚙️ Runtime Ports

Based on `configuration/configurations/*.yml`:

| Service            | Port |
|--------------------|------|
| Config Server      | 8888 |
| Eureka Discovery   | 8761 |
| Customer Service   | 8090 |
| Product Service    | 8020 |
| Order Service      | 8070 |

---

## 🐳 Docker Compose Infrastructure

The provided `docker-compose.yml` includes:

- PostgreSQL (`localhost:5532` → container `5432`)
- MongoDB (`27017`) with Mongo Express UI ([http://localhost:8081](http://localhost:8081))
- MailDev (SMTP: `1025`, Web UI: [http://localhost:1080](http://localhost:1080))

Note: The Config Server is already configured to use Postgres on host port `5532` with `root/password`, matching docker-compose.

---

## 🚀 Quick Start (Local Dev)

### 1. Start Infrastructure

```bash
  docker compose up -d
```
Check:

- <b>PostgreSQL</b>: localhost:5532 (default)

- <b> MongoDB</b>: localhost:27017, Mongo Express UI: http://localhost:8081

- <b> MailDev UI</b>: http://localhost:1080

### 2. Create PostgreSQL Databases
```bash
   psql -h localhost -p 5532 -U root
   CREATE DATABASE product;
   CREATE DATABASE "order";
```

3. Run Services (In Order)
```bash
# Config Server
cd configuration && ./mvnw spring-boot:run

# Eureka
cd discovery && ./mvnw spring-boot:run

# Product Service
cd product && ./mvnw spring-boot:run

# Customer Service
cd customer && ./mvnw spring-boot:run

# Order Service
cd order && ./mvnw spring-boot:run
```

4. Verify Discovery
   Visit http://localhost:8761 to ensure services are registered with Eureka.

---

## ✅ What’s been fixed/added in this iteration
- Order module completed with missing pieces:
  - PaymentMethod enum
  - CustomerResponse DTO and CustomerClient (HTTP via RestTemplate)
  - PaymentRequest and PaymentClient (resilient no-op if payment svc unavailable)
  - AppConfig with RestTemplate bean and JPA auditing enabled
  - OrderMapper now sets totalAmount from the request
- Config Server updated to match docker-compose Postgres and correct URLs:
  - product-service: jdbc:postgresql://localhost:5532/product, username=root, password=password
  - order-service: jdbc:postgresql://localhost:5532/order, username=root, password=password
  - order-service product-url corrected to /api/v1/products
  - discovery-service default-zone corrected to use host:port (http://localhost:8761/eureka)

## 📡 APIs (quick reference)
- Customer Service base: http://localhost:8090/api/v1/customer
- Product Service base: http://localhost:8020/api/v1/products
- Order Service base: http://localhost:8070/api/v1/orders

### Example: Create Product
curl -X POST http://localhost:8020/api/v1/products \
  -H 'Content-Type: application/json' \
  -d '{"name":"Phone","description":"Smartphone","price":499.99,"category":"ELECTRONICS"}'

### Example: Create Customer
curl -X POST http://localhost:8090/api/v1/customer \
  -H 'Content-Type: application/json' \
  -d '{"id":null,"firstName":"John","lastName":"Doe","email":"john@example.com","address":{"street":"1 Main","houseNumber":"10","zipCode":"10001"}}'

### Example: Place Order
curl -X POST http://localhost:8070/api/v1/orders \
  -H 'Content-Type: application/json' \
  -d '{
        "reference":"ORD-001",
        "amount":499.99,
        "paymentMethod":"CREDIT_CARD",
        "customerId":"<CUSTOMER_ID>",
        "products":[{"productId":1,"quantity":1.0}]
      }'

## ⚠️ Notes
- Kafka is referenced by the Order service for producing order confirmations, but no Kafka broker is included in docker-compose today. You can still run the services and place orders; the PaymentClient handles missing payment service gracefully, and Kafka producer will require you to add Kafka later if you need events.
- Ensure the two Postgres databases exist (product and order) before starting their services.