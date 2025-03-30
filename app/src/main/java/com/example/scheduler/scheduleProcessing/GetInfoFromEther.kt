package com.example.scheduler.scheduleProcessing

import android.util.Log
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URL

class GetInfoFromEther {

    fun download(url: String): ArrayList<String>? {


       return downloadParse(url,"h4, div.lead, div.mt-3, div.fs-6, div.opacity-75,div.week2, div.week1")

    }
    fun downloadParse(url: String, cssQuery: String): ArrayList<String>? {
       // Log.d("GetInfoFromEther", url)
        try {
            val htmlContent = downloadHtml(url)
         //   Log.d("GetInfoFromEther", htmlContent)

            // Выполняем парсинг HTML
            val parsedInfo = parseHtml(htmlContent,cssQuery)
            return parsedInfo

        } catch (e: Exception) {
            // Обрабатываем ошибки сетевого запроса или парсинга

            Log.d("ewerGet", "Ошибка: ${e.message}")
            return null
        }

    }



    private fun downloadHtml(url: String): String {
        val res=URL(url).readText()

       // Log.d("GetInfoFromEther", res)
        return res

    }

    // Функция для парсинга HTML страницы и извлечения нужной информации (пример с использованием Jsoup)

    private fun parseHtml(htmlContent: String, cssQuery:String): ArrayList<String>? {
        try {
            // Используем Jsoup для парсинга HTML
            val doc = Jsoup.parse(htmlContent)

            var text =
                doc.select(cssQuery)
            var txt: ArrayList<String> = arrayListOf()
            for (t in text) {
                txt.add(t.tagName()[0] + " " + t.text())
            }
            return txt


        } catch (e: IOException) {
            // Обрабатываем ошибки парсинга
            Log.d("ew", "Ошибка парсинга: ${e.message}")
            return null
        }
    }
}