package com.leoleoli.douban_crawler

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun getDoc(url: String): Document {
    return Jsoup.connect(url).get()
}


fun getDocFromString(text: String): Document {
    return Jsoup.parse(text)
}