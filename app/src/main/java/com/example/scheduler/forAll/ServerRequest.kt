package com.example.scheduler.forAll

import android.util.Log
import com.example.scheduler.scheduleProcessing.Urls
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentRange
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServerRequest {
    private lateinit var client: HttpClient



    suspend fun getRequest(url: String): String? {

        val result: String?
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
                //Log.d("ServerRequest", "Результат get-запроса: $result")

                return result


            } else {
                Log.e("ServerRequest", "Ошибка при выполнении get-запроса: результат null")
                return null

            }

        } catch (e: Exception) {
            Log.e("ServerRequest", "Ошибка при выполнении get-запроса: ${e.message}", e)
            return null
        }

    }

    suspend fun getFromServ(url: String): String? {

        return try {


            val response: HttpResponse = client.get(url)

            if (response.status.value in 200..299) { // Проверка на успешный статус код
                Log.d("ServerRequest", "Ответ: ${response.status.value} ${response.status.description}")
                response.bodyAsText() // Получение тела ответа в виде строки
            } else {
                Log.d("ServerRequest", "Ошибка12: ${response.status.value} ${response.status.description}")
                 null
            }

        } catch (e: Exception) {

            "Ошибка: ${e.message}" // Вернуть сообщение об ошибке
            return null
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

    suspend fun getSuffixList(): ArrayList<String>? {

        val listArray = getFileList()
        if (listArray != null) {

            val suffixList = ArrayList<String>()
            for (item in listArray) {
                suffixList.add(getSuffixByFileName(item))
            }
            return suffixList
        } else
            return null
    }

    suspend fun getFileList(): Array<String>? {
        val filesString = ServerRequest().getRequest(Urls.ServerUrl.url + "list_files")
        if (filesString != null)
            return filesString.split("\n").toTypedArray()
        else {
            Log.d("ServerRequest", "filesList is null")
            return null

        }
    }

    suspend fun findFileBySuffix(suffix: String): String? {
        val listArray = getFileList()
        if (listArray != null) {
            for (item in listArray){
                   // Log.d("ServerRequest !!!","("+getSuffixByFileName(item)+") ["+suffix+"]")
                if (getSuffixByFileName(item)==suffix){

                    return item
                }
            }
        }
        return null
    }
    fun getSuffixByFileName(filename:String):String{
        return filename.substringAfter("-").substringBefore("\n")
    }


}