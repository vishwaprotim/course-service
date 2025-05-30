# course-service
A simple Spring Boot REST application with in-memory collections

## Launching the Application
### From your local system via IDE
Run the application from your IDE and access this API from here: [http://localhost:8080](http://localhost:8080)

### As a Docker Container

Create the image:
```shell
docker build .
```
Now create the container and run it:
```shell
docker build -t course-service:0.0.1 .
```
```shell
docker run -p 8080:8080 course-service:0.0.1 
```

Once the container starts, you can access the API from here: [http://localhost:8080](http://localhost:8080)