package com.leoleoli.douban_crawler

import io.github.cdimascio.dotenv.dotenv
import java.net.URL
import javax.imageio.ImageIO
import java.io.File
import com.rabbitmq.client.Delivery
import java.nio.charset.Charset


fun downloadAndSaveImg(src: String, folder: String) {
    val url = URL(src)
    val imgName = src.split("/").last()
    val imgExt = src.split(".").last()
    val image = ImageIO.read(url)

    val file = File(folder, imgName)
    file.mkdirs()
    ImageIO.write(image, imgExt, file)
    println("Saved to $file")
}

fun main() {

    val dotenv = dotenv()
    val rabbitMQUri = dotenv["AMQP_URI"]?: DEFAULT_URI
    val channel = createChannel(rabbitMQUri)

    val deliverCallback = { consumerTag: String, delivery: Delivery ->
        val photoUrl = String(delivery.body, Charset.forName("UTF-8"))
        println(" [o] Got URL '$photoUrl'")
        val doc = getDoc(photoUrl)
        val imgUrl = getPhotoLink(doc)
        val albumName = getAlbumNameFromPhotoPage(doc)
        println(" [x] Downloading '$imgUrl'")
        downloadAndSaveImg(imgUrl, albumName)
        channel.basicAck(delivery.envelope.deliveryTag, false);
        Thread.sleep(1000)
    }
    channel.basicQos(10)
    channel.basicConsume(QUEUE_NAME, false, deliverCallback, { _ -> })


}