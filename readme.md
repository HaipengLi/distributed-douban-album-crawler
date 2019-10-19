# Distributed Douban Album Crawler
A simple distributed [douban.com](https://www.douban.com/) album crawler in Kotlin, JSoup and RabbitMQ.
## Install Dependencies

1. Download newest `webp-imageio-core` jar file from https://github.com/nintha/webp-imageio-core/releases.
2. Put the jar file under `libs/`.
3. Build with gradle.

## Usage

1. Create a `.env` file in the root of the project directory, with your RabbitMQ URI (see `sample.env`).
2. Run Master
```
gradlew runMaster --args="--url <url_of_album>"
# e.g. gradlew runMaster --args="--url https://www.douban.com/photos/album/1658301994/"
```
3. Run Slave (You can run as many as you want in different machines as long as they can connect to the same RabbitMQ server)
```
# The args is optinal. Default is `out` under current folder.
gradlew runSlave --args="--out <url_of_album>"
```
