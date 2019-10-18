package com.leoleoli.douban_crawler

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory

val DEFAULT_URI = "amqp://guest:guest@localhost"

val QUEUE_NAME = "photo_urls"


fun createChannel(url: String): Channel {
    val factory = ConnectionFactory()
//    factory.host = url
    factory.setUri(url)
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    return channel
}

fun publishStringList(channel: Channel, msgs: List<String>) {
    msgs.forEach { channel.basicPublish("", QUEUE_NAME, null, it.toByteArray()) }
}