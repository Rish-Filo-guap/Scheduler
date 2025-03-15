package com.example.scheduler.scheduleViews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.scheduler.R
import com.example.scheduler.forAll.GetPostSchedule
import com.example.scheduler.forAll.ServerRequest
import com.example.scheduler.scheduleProcessing.GrPrCl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.*
import kotlin.random.Random


class OptionPageFragment(val parent: GetPostSchedule) : Fragment() {
    private val serverUrl = "https://schedule-server-filolio.cloudpub.ru/"


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

            CoroutineScope(Dispatchers.IO).launch {
                val group = parent.getGroup(0)
                var url: String?
                if (group != null) {
                    url = GenUrl(group)
                    if (url != null) {
                        parent.postSchedule(url, 0)
                        Log.d("optionPage", "запрос отправлен")

                    } else {
                        Log.d("optionPage", "не получилось сделать ссылку")
                    }
                } else {
                    Log.d("optionPage", "не указана группа")

                }



                Log.d("ew", "start_upload")
                //makeRequest(url,"get")
                //val res = makeRequest(url, "post")

            }
        }
    }

    private suspend fun GenUrl(group: String): String? {
        var gr=group.substringBefore("-")
        gr=GrPrCl().groups.getOrDefault(gr,GrPrCl().prepods.getOrDefault(gr,"test1234")).substringAfter("=")
        val list = ServerRequest().getRequest(serverUrl + "list_files")
        var suffix=""

        if (list == null)
            Log.d("optionPage", "list is null")
        else{
            val listArray = list.split("\n").toTypedArray()
            var suffixlist=ArrayList<String>()
            for (item in listArray){
                if(item.substringBefore("-")==gr)
                    suffixlist.add(item.substringAfter("-").substringBefore("\n"))
            }
            for (item in suffixlist)
                Log.d("OptionPage", item)
            suffix=genRandomString()
            while (isContainSuffix(suffix, suffixlist)){
                suffix=genRandomString()
            }

        }
        Log.d("OptionPage_url", "${serverUrl}add_schedule/${gr}-$suffix")
        return "${serverUrl}add_schedule/${gr}-$suffix"
    }
    fun genRandomString(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        val stringBuilder = StringBuilder()

        for (i in 0 until 3) {
            val randomIndex = Random.nextInt(alphabet.length)
            stringBuilder.append(alphabet[randomIndex])
        }

        return stringBuilder.toString()
    }
    fun isContainSuffix(suf:String, sufList:ArrayList<String>):Boolean{
        for (item in sufList){
            if (item==suf) return true
        }
        return false
    }

}

