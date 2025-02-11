package com.example.scheduler

import android.content.Context
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URL

class GetInfoFromEther {

    fun Download(url: String):String {


        //CoroutineScope(Dispatchers.IO).launch {
            try {
                val htmlContent = downloadHtml(url)

                // Выполняем парсинг HTML
                val parsedInfo = parseHtml(htmlContent)

                // Обновляем UI в главном потоке
                return parsedInfo



            } catch (e: Exception) {
                // Обрабатываем ошибки сетевого запроса или парсинга

                   return "Ошибка: ${e.message}"

            }

       // }

    }
    private fun downloadHtml(url: String): String {
        return URL(url).readText()
    }

    // Функция для парсинга HTML страницы и извлечения нужной информации (пример с использованием Jsoup)
    private fun parseHtml(htmlContent: String): String {
        try {
            // Используем Jsoup для парсинга HTML
            val doc = Jsoup.parse(htmlContent)

            // Пример: Извлекаем текст из всех элементов <p>
            //all subjects
            //val paragraphs = doc.select("div.lead")

            //class, prep, gr
            //val paragraphs = doc.select("div.opacity-75")

            //numb, timeStart, timeEnd
            //val paragraphs = doc.select("div.mt-3")

            //type subject
            //val paragraphs = doc.select("div.fs-6")


            val paragraphs = doc.select("h4")





            val text = paragraphs.joinToString("\n \n") { it.text() }

            return text

        } catch (e: IOException) {
            // Обрабатываем ошибки парсинга
            return "Ошибка парсинга: ${e.message}"
        }
    }
}