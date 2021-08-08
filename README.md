# Docker
Docker build:
`docker build --build-arg JAR_FILE=target/*.jar -t brettalcox/tesla-inventory:<tag> .`
Docker run:
`docker run -d -p 8080:8080 tesla-inventory`