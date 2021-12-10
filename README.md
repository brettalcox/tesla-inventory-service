# Tesla Inventory Service
Buying a Tesla is quite a bit different than buying any other car brand. Going through the process of custom ordering a Tesla can take weeks to months, and Tesla's estimated delivery dates are of no help either. Enter, "Existing Inventory". The "Existing Inventory" exists on Tesla's website to locate "New" vehicles within a certain radius of your zip code. The vehicles have many different sources--cancelled/refused deliveries, cars that didn't match to nearby customers, influx of new revisions [LFP batteries], demo/test drive vehicles--but can drastically _decrease_ the amount of time you spend waiting for your vehicle. However, it involves lots of tabs, refreshing, and luck.

This tool is simple: it polls Tesla's inventory API and does the heavy lifting for you. The tool looks for "New" Model 3, Model Y, and Model S (no interest in Model X, yet!), and if it finds any, builds a notification and posts to Discord webhooks. This notification contains a significant amount of information about the listing, including trim, exterior color, interior color, wheels, options, FSD yes/no, price, and most importantly, a direct link to the payment page, which is only a few clicks away from being secured!

Tesla Inventory Discord: https://discord.gg/rAkyXCcQCv

## Example Notification
![Screenshot from 2021-09-18 15-51-14](https://user-images.githubusercontent.com/7462622/133908271-ce6c835a-cc42-4d9a-8374-dd363ddf1ae3.png)

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
https://discord.gg/rAkyXCcQCv

## Support
My goal with this tool is to help future Tesla owners get their cars quicker. If you found this useful or were able to secure a car from Existing Inventory, consider donating to fund current and future development to make this tool better :)


https://cash.app/$tootwrangler

https://www.paypal.com/paypalme/brettalcox?locale.x=en_US

https://venmo.com/code?user_id=2061737712943104185&created=1631624986.961526&printed=1
