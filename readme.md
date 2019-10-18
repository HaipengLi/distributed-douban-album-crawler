# Distributed Douban Album Crawler
A simple distributed [douban.com](https://www.douban.com/) album crawler in Kotlin, JSoup and RabbitMQ.
## Install Dependencies

1. Download newest `webp-imageio-core` jar file from https://github.com/nintha/webp-imageio-core/releases.
2. Put the jar file under `libs/`.
3. Build with gradle.

## Usage

1. Create a `.env` file in the root of the project directory, with your RabbitMQ URI (see `sample.env`).
2. 