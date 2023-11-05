## This repo and the hosted instance are no longer maintained -- if you're looking for an active and up-to-date inventory tracker, check out https://waitingfortesla.com/ !

# Tesla Inventory Service
**Featured on Vice!** https://www.vice.com/en/article/4aw843/the-tool-that-helps-people-skip-the-tesla-buyer-waitlist

Buying a Tesla is quite a bit different than buying any other car brand. Going through the process of custom ordering a Tesla can take weeks to months, and Tesla's estimated delivery dates are of no help either. Enter, "Existing Inventory". The "Existing Inventory" exists on Tesla's website to locate "New" vehicles within a certain radius of your zip code. The vehicles have many different sources--cancelled/refused deliveries, cars that didn't match to nearby customers, influx of new revisions [LFP batteries], demo/test drive vehicles--but can drastically _decrease_ the amount of time you spend waiting for your vehicle. However, it involves lots of tabs, refreshing, and luck.

This tool is simple: it polls Tesla's inventory API and does the heavy lifting for you. The tool looks for "New" Model 3, Model Y, and Model S (no interest in Model X, yet!), and if it finds any, builds a notification and posts to Discord webhooks. This notification contains a significant amount of information about the listing, including trim, exterior color, interior color, wheels, options, FSD yes/no, price, and most importantly, a direct link to the payment page, which is only a few clicks away from being secured!

## Example Notification
![Screenshot from 2022-03-26 18-45-52](https://user-images.githubusercontent.com/7462622/160260743-77f20c21-f886-46c0-81d8-7648061a8f66.png)

![image0](https://user-images.githubusercontent.com/7462622/160260857-62561282-9ae3-4526-b038-8a79f528802e.jpeg)

## Building for Local Development
1. Install (at least) Java 11: https://adoptopenjdk.net/installation.html
2. Install MVN: https://maven.apache.org/install.html
3. `git clone https://github.com/brettalcox/tesla-inventory-service.git`
4. `cd tesla-inventory-service`
5. `mvn clean install`

## Building Docker image
1. Make sure you've done the previous section (building the artifact for local development)
2. `docker build --build-arg JAR_FILE=target/*.jar -t tesla-inventory-service:<tag> .` (specify your tag here--2.9.1, 2.9.2 etc, or omit it entirely for `latest`)

## Running Tesla Inventory Service
tesla-inventory-service is setup in a docker-compose. There are 3 containers part of this composition: the app itself, MySQL (for storing referral codes for referral lottery), and Redis (for caching results).
1. Install Docker: https://docs.docker.com/get-docker/
2. Install Docker Compose: https://docs.docker.com/compose/install/
3. Create (or use the existing) docker-compose.yml
```
version: "3.3"
services:
  web:
    restart: "always"
    image: "brettalcox/tesla-inventory:2.9.0"
    ports:
      - "8080:8080"
    environment:
      US_M3SRPRWD: <url>
      US_M3LRAWD: <url>
      US_M3LRAWDP: <url>
      US_MYLRAWD: <url>
      US_MYLRAWDP: <url>
      US_MSPLAID: <url>
      US_MS100DE: <url>
      US_UNKNOWN: <url>
      CA_M3SRPRWD: <url>
      CA_M3LRAWD: <url>
      CA_M3LRAWDP: <url>
      CA_MYLRAWD: <url>
      CA_MYLRAWDP: <url>
      CA_MSPLAID: <url>
      CA_MS100DE: <url>
      CA_UNKNOWN: <url>
      ERROR_NOTIFICATION_URL: <url>
  redis:
    restart: "always"
    image: "redis:latest"
    ports:
      - "6379:6379"
  maria:
    restart: "always"
    image: "mariadb:10.5.12"
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: toot69
      MYSQL_DATABASE: inventory
    volumes:
      - /opt/mysql_data:/var/lib/mysql
```
The `<url>` will need to be supplied if this functionality is desired. Currently, the inventory notifications go to Discord, and the error notifications go to Slack. You'll need to create a Discord and/or Slack channel, and generate some webhook URL's to provide here. 

4. `docker-compose up -d`

## Other Links
https://hub.docker.com/repository/docker/brettalcox/tesla-inventory (this includes linux/amd64 and linux/arm64 for RPi)
