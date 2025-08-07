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

### 🤝 Contributing
1. Fork the repository
2. Create a feature branch (git checkout -b feature/amazing-feature)
3. Commit your changes (git commit -m 'Add some amazing feature')
4. Push to the branch (git push origin feature/amazing-feature)
5. Open a Pull Request
---
