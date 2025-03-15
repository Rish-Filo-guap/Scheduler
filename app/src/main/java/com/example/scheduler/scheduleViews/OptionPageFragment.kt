package com.example.scheduler.scheduleViews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.scheduler.R
import com.example.scheduler.forAll.GetPostSchedule
import com.example.scheduler.forAll.ServerRequest
import com.example.scheduler.scheduleProcessing.GrPrCl
import com.example.scheduler.scheduleProcessing.Urls
import kotlinx.coroutines.*
import kotlin.random.Random


class OptionPageFragment(val parent: GetPostSchedule) : Fragment() {

    private lateinit var codeTextView:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_option_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = runBlocking {
        super.onViewCreated(view, savedInstanceState)
        val shareBtn: Button = view.findViewById(R.id.share_schedule_btn)
        val secShareBtn: Button = view.findViewById(R.id.share_sec_schedule_btn)
        codeTextView=view.findViewById(R.id.code_text_view)
        shareBtn.setOnClickListener {
            shareScheduleFromPage(0)

        }
        secShareBtn.setOnClickListener{
            shareScheduleFromPage(1)
        }
    }
    private fun shareScheduleFromPage(pageNumb:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val group = parent.getGroup(pageNumb)
            var url: String?
            if (group != null) {
                url = genUrl(group)
                if (url != null) {
                    parent.postSchedule(url, pageNumb)
                    Log.d("optionPage", "запрос отправлен")

                } else {
                    Log.d("optionPage", "не получилось сделать ссылку")
                }
            } else {
                Log.d("optionPage", "не указана группа")

            }

        }
    }
    private suspend fun genUrl(group: String): String {
        var gr = group.substringBefore("-")
        gr = GrPrCl().groups.getOrDefault(gr, GrPrCl().prepods.getOrDefault(gr, "test1234"))
            .substringAfter("=")
        val suffixList = ServerRequest().getSuffixList()
        var suffix = ""

        if (suffixList == null)
            Log.d("optionPage", "suffixList is null")
        else {


            suffix = genRandomString()
            if (suffixList.size < 26 * 26 * 26 * 26)
                while (isContainSuffix(suffix, suffixList)) {
                    suffix = genRandomString()
                }
            withContext(Dispatchers.Main) {
                codeTextView.setText(suffix)

                Log.d("optionPage", "код сгенерирован: $suffix")
            }
        }
        Log.d("OptionPage_url", "${Urls.ServerUrl.url}add_schedule/${gr}-$suffix")
        return "${Urls.ServerUrl.url}add_schedule/${gr}-$suffix"
    }


    fun genRandomString(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        val stringBuilder = StringBuilder()

        for (i in 0 until 4) {
            val randomIndex = Random.nextInt(alphabet.length)
            stringBuilder.append(alphabet[randomIndex])
        }

        return stringBuilder.toString()
    }

    private fun isContainSuffix(suf: String, sufList: ArrayList<String>): Boolean {
        for (item in sufList) {
            if (item == suf) return true
        }
        return false
    }

}

