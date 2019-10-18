package com.leoleoli.douban_crawler


fun getAllPhotoLinks(albumUrl: String): List<String> {
    var url: String? = albumUrl
    val photoLinks = mutableListOf<String>()
    while (url != null && url.isNotEmpty()) {
        println("Processing page $url...")
        val doc = getDoc(url)
        url = getNextPageLink(doc)
        photoLinks += getPhtotLinks(doc)
        Thread.sleep(1000)
    }
    return photoLinks
}


fun main() {
//    val html = object {}.javaClass.getResource("柯布西噎死你的相册-一日一画.html").readText()
//    val doc = getDocFromString(html)
    val url = "https://www.douban.com/photos/album/1690398143/"
    val doc = getDoc(url)
    val numPhotos = getNumPhtotsOfAlbum(doc)
    val albumName = getAlbumName(doc)
    val photos = getAllPhotoLinks(url)
    println("$numPhotos photos")
    println("Album name: $albumName")
    println("Num photos on the page: ${photos.size}")
    println("Photo links: $photos")
}