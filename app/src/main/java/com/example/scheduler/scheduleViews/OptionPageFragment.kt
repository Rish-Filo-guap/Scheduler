package com.example.scheduler.scheduleViews

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.scheduler.R
import com.example.scheduler.forAll.GetPostSchedule
import com.example.scheduler.forAll.ServerRequest
import com.example.scheduler.scheduleProcessing.GrPrCl
import com.example.scheduler.scheduleProcessing.Urls
import kotlinx.coroutines.*
import kotlin.random.Random


class OptionPageFragment(val parent: GetPostSchedule) : Fragment() {

    private lateinit var codeTextView: TextView
    private lateinit var copyBtn: ImageButton

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
        copyBtn = view.findViewById(R.id.copy_code_btn)
        codeTextView = view.findViewById(R.id.code_text_view)
        shareBtn.setOnClickListener {
            shareScheduleFromPage(0)

        }
        secShareBtn.setOnClickListener {
            shareScheduleFromPage(1)
        }
        copyBtn.setOnClickListener {
            copyCode()
        }
        codeTextView.setOnClickListener {
            copyCode()
        }
    }

    private fun copyCode() {
        val textToCopy = codeTextView.text.toString().substringAfter(": ")
        Log.d("OptionPage", "[" + textToCopy + "]")
        if (textToCopy.isNotBlank()) {

            // Получаем ClipboardManager (используем context!!)
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Создаем ClipData с текстом для копирования
            val clipData = ClipData.newPlainText("Код", textToCopy)

            // Устанавливаем ClipData в буфер обмена
            clipboardManager.setPrimaryClip(clipData)


            // Опционально: показываем Toast-сообщение об успешном копировании
            Toast.makeText(
                requireContext(),
                "Код скопирован в буфер обмена",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "нет кода",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun shareScheduleFromPage(pageNumb: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val group = parent.getGroup(pageNumb)
            val url: String?
            if (group != null) {
                url = genUrl(group)
                if (url != null) {
                    parent.postSchedule(url, pageNumb)
                    Log.d("optionPage", "запрос отправлен")

                } else {
                    withContext(Dispatchers.Main) {

                        Toast.makeText(context, "Нет соединения", Toast.LENGTH_SHORT).show()
                    }

                    Log.d("optionPage", "не получилось сделать ссылку")
                }
            } else {
                withContext(Dispatchers.Main) {

                    Toast.makeText(context, "не указана группа", Toast.LENGTH_SHORT).show()
                }
                Log.d("optionPage", "не указана группа")

            }

        }
    }

    private suspend fun genUrl(group: String): String? {
        var gr = group.substringBefore("-")
        gr = GrPrCl().groups.getOrDefault(gr, GrPrCl().prepods.getOrDefault(gr, "test1234"))
            .substringAfter("=")
        val suffixList = ServerRequest().getSuffixList()
        var suffix = ""

        if (suffixList == null) {
            Log.d("optionPage", "suffixList is null")
            return null
        } else {


            suffix = genRandomString()
            if (suffixList.size < 26 * 26 * 26 * 26)
                while (isContainSuffix(suffix, suffixList)) {
                    suffix = genRandomString()
                }
            withContext(Dispatchers.Main) {
                codeTextView.setText("Код: " + suffix)

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

