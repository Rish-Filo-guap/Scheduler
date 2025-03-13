package com.example.scheduler.scheduleViews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.scheduler.R
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.*







class OptionPageFragment : Fragment() {

    private lateinit var client:HttpClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_option_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)= runBlocking {
        super.onViewCreated(view, savedInstanceState)
        val shareBtn: Button = view.findViewById(R.id.share_schedule_btn)
        shareBtn.setOnClickListener {
            client = HttpClient(CIO) {
                install(io.ktor.client.plugins.HttpTimeout) {
                    requestTimeoutMillis = 15000 // 15 секунд
                    connectTimeoutMillis = 15000 // 15 секунд
                    socketTimeoutMillis = 15000 // 15 секунд
                }
            }

            val url = "https://schedule-server-filolio.cloudpub.ru/add_schedule/filik"


            Log.d("ew", "start_upload")
                //makeRequest(url,"get")
               val res= makeRequest(url,"post")


        }
    }

    private fun makeRequest(url: String, type:String):String? {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var result:String?=null

                when(type){
                    "get" -> result=getSchedFromServ(url)
                    "post"-> result=postSchedToServ(url)
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

    suspend fun postSchedToServ(url: String): String? {

        return try {

            val response: HttpResponse = client.post(url){
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

