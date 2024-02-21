# spring-boot-kafka-rest-proxy-poc

```
docker-compose up -d

cd producer
mvn spring-boot:run

open http://localhost:8080/swagger-ui.html

curl -X 'POST' \
  'http://localhost:8080/event' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "string",
  "description": "string",
  "sentBy": "string"
}'

curl -X 'POST' \
  'http://localhost:8080/topics/topic.one/records' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json'

cd consumer
mvn spring-boot:run
```

# References