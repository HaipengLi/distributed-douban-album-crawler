@file:JvmName("MasterCrawler")  // For gradle run task
package com.leoleoli.douban_crawler

import com.rabbitmq.client.Channel
import io.github.cdimascio.dotenv.dotenv
import kotlinx.cli.CommandLineInterface
import kotlinx.cli.flagValueArgument
import kotlinx.cli.parse
import java.security.InvalidParameterException
import kotlin.system.exitProcess


fun getAllPhotoLinks(albumUrl: String, publish: Boolean = false, channel: Channel? = null): List<String> {
    var url: String? = albumUrl
    val photoLinks = mutableListOf<String>()
    while (url != null && url.isNotEmpty()) {
        println("Processing page $url...")
        val doc = getDoc(url)
        url = getNextPageLink(doc)
        val photoLinksOnPage = getPhotoLinks(doc)
        if (publish) {
            publishStringList(channel!!, photoLinksOnPage)
        }
        photoLinks += photoLinksOnPage
        Thread.sleep(1000)
    }
    return photoLinks
}


fun main(args: Array<String>) {
    val cli = CommandLineInterface("MasterCrawler")
    val url by cli.flagValueArgument(
        "--url", "url", "The url of the album from douban.com")

    try {
        cli.parse(args)
    }
    catch (e: Exception) {
        exitProcess(1)
    }
    val dotenv = dotenv()

    val doc = getDoc(url!!)
    val numPhotos = getNumPhotosOfAlbum(doc)
    val albumName = getAlbumName(doc)
    println("$numPhotos photos")
    println("Album name: $albumName")

    val rabbitMQUri = dotenv["AMQP_URI"]?: DEFAULT_URI
    println("Connecting to $rabbitMQUri")

    val channel = createChannel(rabbitMQUri)
    val photos = getAllPhotoLinks(url!!, publish = true, channel = channel)
    println("Successfully published ${photos.size}!")
    channel.connection.close()
}