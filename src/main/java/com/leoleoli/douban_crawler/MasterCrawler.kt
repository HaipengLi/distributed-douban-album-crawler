package com.leoleoli.douban_crawler

import com.rabbitmq.client.Channel
import io.github.cdimascio.dotenv.dotenv


fun getAllPhotoLinks(albumUrl: String, publish: Boolean = false, channel: Channel? = null): List<String> {
    var url: String? = albumUrl
    val photoLinks = mutableListOf<String>()
    while (url != null && url.isNotEmpty()) {
        println("Processing page $url...")
        val doc = getDoc(url)
        url = getNextPageLink(doc)
        val photoLinksOnPage = getPhtotLinks(doc)
        if (publish) {
            publishStringList(channel!!, photoLinksOnPage)
        }
        photoLinks += photoLinksOnPage
        Thread.sleep(1000)
    }
    return photoLinks
}


fun main() {
    val dotenv = dotenv()
//    val html = object {}.javaClass.getResource("柯布西噎死你的相册-一日一画.html").readText()
//    val doc = getDocFromString(html)

    val url = "https://www.douban.com/photos/album/1658301994/"
    val doc = getDoc(url)
    val numPhotos = getNumPhtotsOfAlbum(doc)
    val albumName = getAlbumName(doc)
    println("$numPhotos photos")
    println("Album name: $albumName")

    val rabbitMQUri = dotenv["AMQP_URI"]?: DEFAULT_URI
    println("Connecting to $rabbitMQUri")

    val channel = createChannel(rabbitMQUri)
    val photos = getAllPhotoLinks(url, publish = true, channel = channel)
    println("Num photos on the page: ${photos.size}")
    println("Photo links: $photos")
}