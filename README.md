# Apache Camel + Spring Boot + DataWeave Demo

## Overview

This is a **proof-of-concept (POC)** application to see how Camel, Spring Boot and DataWeave can work with each other.

There are still a lot of TODOs to be done in this POC.

- Refactor. All endpoints (except hello) uses the same processor. Make this cleaner.
- Implement other HTTP methods
- Implement error handlers

## Technologies Used

- Java 17
- Spring Boot 3
- Apache Camel
- H2 in-memory database
- DataWeave

## How to run and test locally

Step 1: Clone this project.

Step 2: Open terminal and run the app:

```sh
mvn clean spring-boot:run
```

Step 3: Once the app is running, use Postman or curl to hit the API endpoints.

```sh
curl -i -X GET localhost:9000/hello
curl -i -X GET localhost:9000/employees
curl -i -X GET localhost:9000/employees/1
curl -i -X POST localhost:9000/employees
```

The project uses an H2 database with preloaded data using `data.sql` under `src/main/resources`.

## Documentation on how this project was made

### 1 - Generate the Base Project

Generate the base project using the official Camel archetype for Spring Boot. Command below creates Spring Boot 3 project with Camel starter.

```sh
mvn archetype:generate \
-DarchetypeGroupId=org.apache.camel.archetypes \
-DarchetypeArtifactId=camel-archetype-spring-boot \
-DarchetypeVersion=4.0.0-M3 \
-DgroupId=com.example \
-DartifactId=sb-camel-weave-demo \
-Dversion=1.0.0
```

This uses the latest **Camel-Spring Boot** starter. Spring Boot version is `3.0.6` and Java version is `17`.

### 2 - Add Necessary Dependencies

Once the base project is created, add the following depencies:

- `Spring Data JPA` -> for automatic datasource management
- `H2 database` -> for easy database setup
- `Camel SQL starter` -> to enable Camel SQL component for database queries
- `Camel Undertow starter` -> to use Undertow web server instead of Tomcat
- `Camel Jackson starter` -> for automatic marshalling/unmarshalling of JSON and Java objects
- `DataWeave libraries` -> for data transformation

```xml
    <!-- For database transactions -->
    <dependency>
        <groupId>org.apache.camel.springboot</groupId>
        <artifactId>camel-sql-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- For REST configuration -->
    <dependency>
        <groupId>org.apache.camel.springboot</groupId>
        <artifactId>camel-undertow-starter</artifactId>
    </dependency>
   <!-- Add camel-jackson-starter to allow Camel to convert data to/from JSON -->
  <dependency>
        <groupId>org.apache.camel.springboot</groupId>
        <artifactId>camel-jackson-starter</artifactId>
  </dependency>

    <!-- DataWeave -->
    <dependency>
        <groupId>org.mule.weave</groupId>
        <artifactId>core</artifactId>
        <version>2.6.0-20230426</version>
    </dependency>
    <dependency>
        <groupId>org.mule.weave</groupId>
        <artifactId>core-modules</artifactId>
        <version>2.6.0-20230426</version>
    </dependency>
    <dependency>
        <groupId>org.mule.weave</groupId>
        <artifactId>yaml-module</artifactId>
        <version>2.6.0-20230426</version>
    </dependency>
    <dependency>
        <groupId>org.mule.weave</groupId>
        <artifactId>java-module</artifactId>
        <version>2.6.0-20230426</version>
    </dependency>
    <dependency>
        <groupId>org.mule.weave</groupId>
        <artifactId>runtime</artifactId>
        <version>2.6.0-20230426</version>
    </dependency>
```

### 3 - Update application.properties

- Provide a Camel REST API port so we can access the API.
- Add the H2 database configuration.

```yaml
# rest port
camel.rest.port: 9000

# datasource
spring.datasource.url: jdbc:h2:mem:cameldb
spring.datasource.driverClassName: org.h2.Driver
spring.datasource.username: test
spring.datasource.password:
spring.jpa.database-platform: org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto: create
spring.jpa.show-sql: true
spring.jpa.properties.hibernate.format_sql: true
spring.h2.console.enabled: true
```

### 4 - Build the Camel Routes

Check the `BaseRouter` and `RounterImpl` classes.

Since this is a REST API example, we use `RestConfiguration` and set the component to `undertow` instead of `servlet`.

Create the HTTP methods and their corresponding Camel routes.

### 5 - Externalize the SQL queries

Instead of hardcoding the database queries, create SQL files and store them in the `resources` folder so they are part of the classpath.

Much easier to maintain and adjust queries if they are externalized.

### 6 - Create the DataWeave scripts

Create the DW scripts and store them in the `resources` folder. Having them in the classpath makes it easier to access them for data transformation.

For the DW scripts, nothing special to do. Just create DW scripts normally and test using online DW Playground.

### 7 - Create the DataTransformer class

The `DataTransformer` class handles the DW transformations. It initializes the necessary DW components that will interpret the DW script and accepts the payload that will be transformed.

There are 3 methods inside the DataTransformer class in this POC. The recommended one to use is the `transformFromFile(file, exchange)` method. This accepts the DW script to use and the Camel Exchange message that contains the payload to transform.

## References

- <https://mvnrepository.com/artifact/org.apache.camel.springboot/camel-spring-boot-starter>
- <https://github.com/qureshii/springboot-dataweave>

## Contact

[Andie Azucena](mailto:andie.azucena@outlook.com)
