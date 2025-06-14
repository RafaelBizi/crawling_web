# Backend-test

## Description

This project is a web application that performs the functionality of crawling. It uses the Breadth-First Search (BFS) algorithm to perform breadth search. The application was built following the principles of SOLID, Clean Code, Clean Architecture and DDD (Domain-Driven Design).
## Technologies and Languages

The project was developed using the following technologies and languages:
- Java: Programming language used to develop the application.
- Spark: Web framework used to create the REST API.
- Gson: Library used to serialize and deserialize JSON objects.
- Maven: Dependency manager used to manage the project's dependencies.
- JUnit: Framework used to create unit tests.
- Mockito: Framework used to create mock objects in unit tests.

## How to build the application

To build the application, you need to have the following tools installed on your machine:

```bash
docker build . -t crawling/backend
```

## How to run the application

To run the application, you need to have the following tools installed on your machine, where "BASE_URL" is the URL of the website you want to crawl:

```bash
docker run -e BASE_URL=https://github.com/RafaelBizi/crawling_web -p 4567:4567 --rm crawling/backend
```

