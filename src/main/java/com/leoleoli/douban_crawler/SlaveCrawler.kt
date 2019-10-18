package com.leoleoli.douban_crawler

import java.net.URL
import javax.imageio.ImageIO
import java.io.File




fun downloadAndSaveImg(src: String, folder: String) {
    val url = URL(src)
    val imgName = src.split("/").last()
    val imgExt = src.split(".").last()
    val image = ImageIO.read(url)

    val file = File(folder, imgName)
    ImageIO.write(image, imgExt, file)
    println("Saved to $file")
}

fun main() {
    val url = "https://www.douban.com/photos/photo/2568088493/"
    val doc = getDoc(url)
    val imgUrl = getPhotoLink(doc)
    downloadAndSaveImg(imgUrl, ".")
}