# 📚 Library Event Producer API

Spring boot microservice which produces Kafka events for book registrations and updates in a library system.

## 🛠️ Tech Stack

- **Backend**: Spring Boot 4.0.3, Java 21 ⚙️
- **Messaging**: Kafka Producer/Consumer APIs 📨
- **Build**: Gradle 🔨
- **Containerization**: Docker + docker-compose 🐳
- **Testing**: JUnit 5, Testcontainers, Integration Tests ✅

## 📋 Features Implemented

- [x] **Add new Book**: Endpoint to create new book registrations that triggers a Kafka event
- [x] **Update Existing Book**: Endpoint to update existing book information that triggers a Kafka event
- [ ] **Remove a Book**
- [ ] **Retrieve list of Books**

## 🔧 How It Works

The application implements two main endpoints:

1. **POST /v1/libraryevent** - Creates a new book registration event
   - Accepts a LibraryEvent object with book details
   - Sends an asynchronous message to Kafka topic using Spring KafkaTemplate
   - Returns HTTP 201 Created on success

2. **PUT /v1/libraryevent** - Updates an existing book registration
   - Requires a libraryEventId in the request
   - Only accepts EventType.UPDATE events
   - Sends an asynchronous message to Kafka topic
   - Returns HTTP 200 OK on success

## 📦 Kafka Integration

The application uses Spring's KafkaTemplate to send messages asynchronously to a Kafka topic:
- Two different asynchronous approaches are implemented in LiveEventsProducer:
  - Approach 1: Using kafkaTemplate.send(topic, key, value)
  - Approach 2: Using kafkaTemplate.send(ProducerRecord)
- Both approaches use CompletableFuture to handle success/failure callbacks
- Messages are serialized as JSON using Jackson ObjectMapper

## 🧪 Testing

The project includes:
- Unit tests for the controller layer using @WebMvcTest and Mockito
- Integration tests using Testcontainers for Kafka
- Validation tests for both successful and error scenarios

## ▶️ Getting Started

1. Ensure you have Java 21 installed
2. Ensure Docker is running
3. Start Kafka cluster using Docker Compose:
   ```
   docker-compose up -d
   ```
4. Build the project: `./gradlew build`
5. Run the application: `./gradlew bootRun`
6. The service will be available at http://localhost:8080

## 🐳 Docker Compose Setup

To run the Kafka cluster locally, you'll need a docker-compose.yml file. Based on the application configuration, Kafka is expected to be available at:
- localhost:9092
- localhost:9093
- localhost:9094

You can use a docker-compose file similar to this:

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "22181:2181"

  kafka1:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka1:9092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

  kafka2:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9093:9093"
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka2:9093,PLAINTEXT_HOST://localhost:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

  kafka3:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9094:9094"
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka3:9094,PLAINTEXT_HOST://localhost:9094
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

## 📝 API Examples

Create a new book:
```http
POST /v1/libraryevent
Content-Type: application/json

{
  "libraryEventId": null,
  "libraryEventType": "NEW",
  "book": {
    "bookId": 123,
    "bookName": "Spring Boot in Action",
    "bookAuthor": "Craig Walls"
  }
}
```

Update an existing book:
```http
PUT /v1/libraryevent
Content-Type: application/json

{
  "libraryEventId": 456,
  "libraryEventType": "UPDATE",
  "book": {
    "bookId": 123,
    "bookName": "Spring Boot in Action (Updated)",
    "bookAuthor": "Craig Walls"
  }
}
```