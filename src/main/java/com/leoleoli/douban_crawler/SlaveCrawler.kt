@file:JvmName("SlaveCrawler")  // For gradle run task
package com.leoleoli.douban_crawler

import io.github.cdimascio.dotenv.dotenv
import java.net.URL
import javax.imageio.ImageIO
import java.io.File
import com.rabbitmq.client.Delivery
import kotlinx.cli.CommandLineInterface
import kotlinx.cli.flagValueArgument
import kotlinx.cli.parse
import java.nio.charset.Charset
import kotlin.system.exitProcess


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

fun main(args: Array<String>) {
    val cli = CommandLineInterface("SlaveCrawler")
    val outputFolder by cli.flagValueArgument(
        "--out", "outputFolder", "The folder you want to save the photos.", "out")

    try {
        cli.parse(args)
    }
    catch (e: Exception) {
        exitProcess(1)
    }
    val dotenv = dotenv()
    val rabbitMQUri = dotenv["AMQP_URI"]?: DEFAULT_URI
    val channel = createChannel(rabbitMQUri)

    val deliverCallback = { consumerTag: String, delivery: Delivery ->
        val photoUrl = String(delivery.body, Charset.forName("UTF-8"))
        println(" [o] Got URL '$photoUrl'")
        val doc = getDoc(photoUrl)
        val imgUrl = getPhotoLink(doc)
        val albumName = getAlbumNameFromPhotoPage(doc)
        val saveFolder = File(outputFolder, albumName)
        println(" [x] Downloading '$imgUrl'")
        downloadAndSaveImg(imgUrl, saveFolder.path)
        channel.basicAck(delivery.envelope.deliveryTag, false)
        Thread.sleep(1000)
    }
    channel.basicQos(10)
    channel.basicConsume(QUEUE_NAME, false, deliverCallback, { _ -> })
}