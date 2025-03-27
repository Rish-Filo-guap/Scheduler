package com.example.scheduler.scheduleViews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.scheduler.R
import com.example.scheduler.forAll.ChangeTabByCode
import com.example.scheduler.forAll.ServerRequest
import com.example.scheduler.scheduleProcessing.CreateScheduleFromParsed
import com.example.scheduler.scheduleProcessing.DaysOfWeek
import com.example.scheduler.scheduleProcessing.GetInfoFromEther
import com.example.scheduler.scheduleProcessing.GrPrCl
import com.example.scheduler.scheduleProcessing.Para
import com.example.scheduler.scheduleProcessing.ScheduleList
import com.example.scheduler.forAll.ShowBottomFragmentDialogParaInfo
import com.example.scheduler.scheduleProcessing.Urls
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter

class ScheduleLayout(context: Context, val parent: ShowBottomFragmentDialogParaInfo) :
    LinearLayout(context) {
    lateinit var schedule: ScheduleList
    val layoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.BLACK)
    }

    fun downloadSchedule(fileInputStream: FileInputStream, group: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val createdSchedule = CreateScheduleFromParsed().ReadSchedule(fileInputStream)
            if (createdSchedule == null) {
                downloadSchedule(group)
            } else {
                withContext(Dispatchers.Main) {
                    schedule = createdSchedule

                    drawSchedule(schedule)
                    invalidate()

                }

            }
        }


    }

    fun downloadSchedule(group: String?) {
        startDownloading()
        if (group == null) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            var newGroup: String
            if (GrPrCl().groups.containsKey(group))
                newGroup = GrPrCl().groups.get(group)!!
            else
                newGroup = GrPrCl().prepods.get(group)!!

            val parsedInfo = GetInfoFromEther().download("https://guap.ru/rasp/?" + newGroup)


            try {
                schedule = CreateScheduleFromParsed().CreateSchedule(parsedInfo!!)

            } catch (e: Exception) {
                Log.d("ScheduleLayout", e.message.toString())
            }
            withContext(Dispatchers.Main) {
                drawSchedule(schedule)
                setBackgroundColor(Color.BLACK)
                invalidate()


            }
        }
    }

    fun downloadScheduleFromServ(newCode: String, parent: ChangeTabByCode) {
        startDownloading()
        CoroutineScope(Dispatchers.IO).launch {
            val filename = ServerRequest().findFileBySuffix(newCode)
            val groupName: String?
            if (filename == null) {
                withContext(Dispatchers.Main) {
                    setBackgroundColor(Color.BLACK)
                    Toast.makeText(context, "Ничего не найдено или нет соединения", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("ScheduleLayout", filename.toString())

                val result =
                    ServerRequest().getRequest(Urls.ServerUrl.url + "add_schedule/" + filename)

                if (result != null) {
                    groupName = GrPrCl().getKeyByValue(filename.substringBefore("-"))

                    val parsedInfo = ArrayList<String>()
                    parsedInfo.addAll(result.split("\n").toTypedArray())
                    parsedInfo.removeAt(parsedInfo.size - 1)


                    schedule = CreateScheduleFromParsed().CreateSchedule(parsedInfo)
                    withContext(Dispatchers.Main) {
                        drawSchedule(schedule)
                        setBackgroundColor(Color.BLACK)

                        parent.changeGroup(groupName ?: "()")
                        invalidate()
                    }


                } else {
                    withContext(Dispatchers.Main) {
                        setBackgroundColor(Color.BLACK)
                        Toast.makeText(context, "Не получилось скачать", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

    private fun startDownloading() {
        schedule = ScheduleList()
        setBackgroundResource(R.color.paraBackGroundColor)
        invalidate()
    }

    fun drawSchedule() {
        drawSchedule(schedule)
    }

    @SuppressLint("ResourceType")
    private fun drawSchedule(schedule: ScheduleList) {
        removeAllViews()
        val textView = TextView(context)

        addView(textView)
        // Получаем текущую дату
        var currentDate = now()
        // Форматируем дату
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")


        // Добавляем дни недели в список (например, на 30 дней вперед)

        for (i in 0 until 21) {
            // Создаем TextView для отображения дня недели и даты
            val dateTextView = TextView(context)

            val formattedDate = currentDate.format(dateFormatter)

            val weekNumb = ((currentDate).format(DateTimeFormatter.ofPattern("w"))
                .toInt() + 1) % 2
            val dayOfWeekNumb =
                ((currentDate.minusDays(1)).format(DateTimeFormatter.ofPattern("e"))).toInt() - 1
            val dayOfWeek = DaysOfWeek.values()[dayOfWeekNumb].dayOfWeek

            dateTextView.text = "$dayOfWeek $formattedDate"
            dateTextView.textSize = 20f
            dateTextView.setPadding(100, 40, 16, 16)
            addView(dateTextView, layoutParams)

            val linearLayoutParas = LinearLayout(context)
            linearLayoutParas.orientation = VERTICAL


            //основное расписание
            for (j in 0..schedule.days[dayOfWeekNumb].paras.size - 1) {
                val paraLinearLayout =
                    GenParaSchedule(schedule.days[dayOfWeekNumb].paras[j], weekNumb)
                if (paraLinearLayout != null)
                    linearLayoutParas.addView(paraLinearLayout, layoutParams)
            }

            if (linearLayoutParas.childCount == 0) {

                linearLayoutParas.addView(WeekendLinearLayoutCreator(context).getLinearLayout()!!)
            }
            addView(linearLayoutParas, layoutParams)

            currentDate = currentDate.plusDays(1)
        }

        val textViewOutside = TextView(context)
        textViewOutside.text = DaysOfWeek.values()[7].dayOfWeek
        textViewOutside.textSize = 20f
        textViewOutside.setPadding(100, 40, 16, 16)

        addView(textViewOutside, layoutParams)
        val linearLayoutParas = LinearLayout(context)
        linearLayoutParas.orientation = VERTICAL

        //вне сетки расписания
        for (j in 0..schedule.days[7].paras.size - 1) {
            val paraLinearLayout = GenParaSchedule(schedule.days[7].paras[j], 0)
            if (paraLinearLayout != null)
                linearLayoutParas.addView(paraLinearLayout, layoutParams)
        }

        setPadding(20, 0, 20, 0)
        addView(linearLayoutParas, layoutParams)

    }

    @SuppressLint("ResourceAsColor")
    private fun GenParaSchedule(para: Para, weekNumb: Int): LinearLayout? {
        if (para.weekType != weekNumb + 1 && para.weekType != 0) return null
        else {

            val linearLayoutCreator = ParaLinearLayoutCreator(context, para, parent)

            val paraLinearLayout: LinearLayout? = linearLayoutCreator.getLinearLayout()
            return paraLinearLayout
        }
    }
}
