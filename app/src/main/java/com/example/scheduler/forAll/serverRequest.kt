package com.example.scheduler.forAll

import android.util.Log
import io.ktor.client.HttpClient
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

class serverRequest {
    private lateinit var client: HttpClient

    private fun makeRequest(url: String, type: String): String? {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var result: String? = null

                when (type) {
                    "get" -> result = getSchedFromServ(url)
                    "post" -> result = postSchedToServ(url)
                }


                if (result != null) {
                    Log.d("OptionPage", "Результат $type-запроса: $result")

                } else {
                    Log.e("OptionPage", "Ошибка при выполнении $type-запроса: результат null")

                }
                withContext(Dispatchers.Main) {
                    return@withContext result
                }
            } catch (e: Exception) {
                Log.e("OptionPage", "Ошибка при выполнении $type-запроса: ${e.message}", e)

            }
        }
        return null
    }

    suspend fun getSchedFromServ(url: String): String? {

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


    fun postSchedule(url: String, schedule: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var result: String? = null
                result = postSchedToServ(url)

                if (result != null) {
                    Log.d("OptionPage", "Результат post-запроса: $result")

                } else {
                    Log.e("OptionPage", "Ошибка при выполнении post-запроса: результат null")

                }

            } catch (e: Exception) {
                Log.e("OptionPage", "Ошибка при выполнении post-запроса: ${e.message}", e)

            }
        }
    }

    suspend fun postSchedToServ(url: String): String? {

        return try {

            val response: HttpResponse = client.post(url) {
                contentType(ContentType.Text.Any)
                setBody("qwuieuoqeuicudoisfuiosd")
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