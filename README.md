# Docker
Docker build:
`docker build --build-arg JAR_FILE=target/*.jar -t tesla-inventory .`
Docker run:
`docker run -d -p 8080:8080 tesla-inventory`