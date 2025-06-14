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
docker build . -t axreng/backend
```

## How to run the application

To run the application, you need to have the following tools installed on your machine, where "BASE_URL" is the URL of the website you want to crawlbash

```bash
docker run -e BASE_URL=http://hiring.axreng.com/ -p 4567:4567 --rm axreng/backend
```

## API Endpoints

The application exposes two REST endpoints:

### Start Crawling Process
Initiates a new crawling process for a specific keyword.

**Endpoint:** `POST http://localhost:4567/crawl`

**Request Body:**
{ "keyword": "string" }

**Response Body:**
{ "id": "8c7e4dfd-927a-4b5e-8398-c4964f6275c3", "status": "active" }


### Get Crawling Results
Retrieves the results of a specific crawling process.

**Endpoint:** `GET http://localhost:4567/crawl/:id`

**Response Example (200 OK):**

{
"id": "7TJBOJ2B",
"status": "active",
"urls": [
"http://hiring.axreng.com/",
"http://hiring.axreng.com/index.html",
"http://hiring.axreng.com/htmlman8/agetty.8.html",
"http://hiring.axreng.com/htmlman8/ld.so.8.html",
"http://hiring.axreng.com/htmlman5/proc.5.html",
"http://hiring.axreng.com/htmlman7/feature_test_macros.7.html",
"http://hiring.axreng.com/htmlman2/bpf.2.html",
"http://hiring.axreng.com/htmlman7/pty.7.html",
"http://hiring.axreng.com/htmlman2/gettimeofday.2.html"
]
}

## Usage Example
1. First, start the application using Docker as described above.
2. Start a new crawling process for the keyword "compilation":

```bash
curl -X POST http://localhost:4567/crawl -H "Content-Type: application/json" -d '{"keyword": "compilation"}'
```

3. You'll receive a response with an ID, like:
{ "id": "7TJBOJ2B" } 

4. Use this ID to check the crawling results:
```bash
curl http://localhost:4567/crawl/7TJBOJ2B
```

5. The response will show all URLs containing the word "compilation":
   {
   "id": "7TJBOJ2B",
   "status": "active",
   "urls": [
   "http://hiring.axreng.com/",
   "http://hiring.axreng.com/index.html",
   "http://hiring.axreng.com/htmlman8/agetty.8.html",
   "http://hiring.axreng.com/htmlman8/ld.so.8.html",
   "http://hiring.axreng.com/htmlman5/proc.5.html",
   "http://hiring.axreng.com/htmlman7/feature_test_macros.7.html",
   "http://hiring.axreng.com/htmlman2/bpf.2.html",
   "http://hiring.axreng.com/htmlman7/pty.7.html",
   "http://hiring.axreng.com/htmlman2/gettimeofday.2.html"
   ]
   }

Note: The crawling process runs asynchronously. When you first check the results, the status might still be "active". Keep checking until the status changes to "done" to see all discovered URLs.


