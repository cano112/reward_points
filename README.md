# Reward Points

Service responsible for tracking reward points awarded when creating transactions.

## Local development

### Requirements
* Java 21
* Docker Desktop (or any other Docker installation [detectable by testcontainers](https://golang.testcontainers.org/features/configuration/#docker-host-detection))

### Running app

```bash
./gradlew bootTestRun
```

### Running tests
```bash
./gradlew test
```

### API documentation

* OpenAPI: http://localhost:8080/v3/api-docs
* Swagger UI: http://localhost:8080/swagger-ui/index.html