## Massive Open Online Courses (MOOC) API using Java, Spring ecosystem 

### What's new? (Technical)

- Use new JdbcClient
- Implement basic OAuth2 server.
- [Experiment] Apply native filtering and pagination at the framework level instead of the business code level by introducing the **JdbcClient SQL interpreter**.
- use Docker Compose support in Spring Boot 3.1
- use new String template (STR Template Processor) in Java 21

### How to run?

```shell
    docker compose up
    .\mvnw spring-boot:run
```

### API docs

- Swagger-ui: http://localhost:8081/swagger-ui/index.html
- Swagger api: http://localhost:8081/v3/api-docs
