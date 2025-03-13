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

    private lateinit var client: HttpClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_option_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = runBlocking {
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
            //val res = makeRequest(url, "post")


        }
    }


}

