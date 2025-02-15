package com.example.scheduler.ScheduleProcessing

import android.util.Log
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URL

class GetInfoFromEther {

    fun Download(url: String): ArrayList<String>? {

            try {
                val htmlContent = downloadHtml(url)

                // Выполняем парсинг HTML
                val parsedInfo = parseHtml(htmlContent)
                return parsedInfo

            } catch (e: Exception) {
                // Обрабатываем ошибки сетевого запроса или парсинга

                Log.d("ewerGet",  "Ошибка: ${e.message}")
                return null
            }

    }
    private fun downloadHtml(url: String): String {
        return URL(url).readText()
    }

    // Функция для парсинга HTML страницы и извлечения нужной информации (пример с использованием Jsoup)
    private fun parseHtml(htmlContent: String): ArrayList<String>? {
        try {
            // Используем Jsoup для парсинга HTML
            val doc = Jsoup.parse(htmlContent)

            var text=doc.select("h4, div.lead, div.mt-3, div.fs-6, div.opacity-75,div.week2, div.week1")
            var txt:ArrayList<String> = arrayListOf()
            for (t in text){
               txt.add(t.tagName()[0]+" "+t.text())
            }
            return txt


        } catch (e: IOException) {
            // Обрабатываем ошибки парсинга
            Log.d("ew", "Ошибка парсинга: ${e.message}")
            return null
        }
    }
}