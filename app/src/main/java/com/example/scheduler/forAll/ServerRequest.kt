package com.example.scheduler.forAll

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServerRequest {
    private lateinit var client: HttpClient

    private fun makeRequest(url: String, type: String): String? {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var result: String? = null

                when (type) {
                    "get" -> result = getFromServ(url)
                    //"post" -> result = postSchedToServ(url)
                }


                if (result != null) {
                    Log.d("ServerRequest", "Результат $type-запроса: $result")

                } else {
                    Log.e("ServerRequest", "Ошибка при выполнении $type-запроса: результат null")

                }
                withContext(Dispatchers.Main) {
                    return@withContext result
                }
            } catch (e: Exception) {
                Log.e("ServerRequest", "Ошибка при выполнении $type-запроса: ${e.message}", e)

            }
        }
        return null
    }

    suspend fun getRequest(url: String): String? {

        var result: String? = null
        try {
            client = HttpClient(CIO) {
                install(io.ktor.client.plugins.HttpTimeout) {
                    requestTimeoutMillis = 15000 // 15 секунд
                    connectTimeoutMillis = 15000 // 15 секунд
                    socketTimeoutMillis = 15000 // 15 секунд
                }
            }

            result = getFromServ(url)

            if (result != null) {
                Log.d("ServerRequest", "Результат get-запроса: $result")

                    return result


            } else {
                Log.e("ServerRequest", "Ошибка при выполнении get-запроса: результат null")

            }

        } catch (e: Exception) {
            Log.e("ServerRequest", "Ошибка при выполнении get-запроса: ${e.message}", e)

        }

        return null
    }

    suspend fun getFromServ(url: String): String? {

        return try {


            val response: HttpResponse = client.get(url)

            if (response.status.value in 200..299) { // Проверка на успешный статус код
                response.bodyAsText() // Получение тела ответа в виде строки
            } else {
                "Ошибка: ${response.status.value} ${response.status.description}" // Вернуть сообщение об ошибке
            }

        } catch (e: Exception) {
            "Ошибка: ${e.message}" // Вернуть сообщение об ошибке
        } finally {
            client.close() // Закрыть клиент после завершения
        }
    }


    suspend fun postRequest(url: String, scheduleString: String) {

        try {
            client = HttpClient(CIO) {
                install(io.ktor.client.plugins.HttpTimeout) {
                    requestTimeoutMillis = 15000 // 15 секунд
                    connectTimeoutMillis = 15000 // 15 секунд
                    socketTimeoutMillis = 15000 // 15 секунд
                }
            }

            var result: String? = null
            result = postSchedToServ(url, scheduleString)

            if (result != null) {
                Log.d("ServerRequest", "Результат post-запроса: $result")

            } else {
                Log.e("ServerRequest", "Ошибка при выполнении post-запроса: результат null")

            }

        } catch (e: Exception) {
            Log.e("ServerRequest", "Ошибка при выполнении post-запроса: ${e.message}", e)

        }

    }

    suspend fun postSchedToServ(url: String, scheduleString: String): String? {

        return try {

            val response: HttpResponse = client.post(url) {
                contentType(ContentType.Text.Any)
                setBody(scheduleString)
            }


            if (response.status.value in 200..299) { // Проверка на успешный статус код
                response.bodyAsText() // Получение тела ответа в виде строки
            } else {
                "Ошибка: ${response.status.value} ${response.status.description}" // Вернуть сообщение об ошибке
            }

        } catch (e: Exception) {
            "Ошибка: ${e.message}" // Вернуть сообщение об ошибке
        } finally {
            client.close() // Закрыть клиент после завершения
        }
    }


}