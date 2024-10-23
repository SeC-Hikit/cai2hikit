# Hikit - cai2hikit microservice

Hikit cai2hikit is a microservice responsible for retrieving data from [cai API](https://osm2cai.cai.it/api/) that will serve to integrate cai data with the Hikit service.

## Build and Run

### Prerequisites
- JDK 21
- Maven 3

### Development build

Simply run
```
./mvnw spring-boot:run
```

### Deployment build

It is possible to build a portable .jar file with

```
./mvnw clean package
```
and then run with
```
java -jar target/cai2hikit-[VERSION]-SNAPSHOT.jar
```

### Test

## API

By default, the openAPI v3 specs are exposed at `/api/v1/api-docs`.

A swagger web interface for the RESTful API can be found at `/swagger-ui/index.html`.