package com.example.scheduler

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.time.LocalDate.now
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем ScrollView
        val scrollView = ScrollView(this)
        var schedule=ScheduleList()

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black)) //настройка цвета там, где у телефона часы, ну короч сверху
        linearLayout.setBackgroundColor(Color.BLACK) //настройка фона всей приложеньки
        // Параметры LinearLayout








        scrollView.addView(linearLayout)


        val getInfoFromEther=GetInfoFromEther()
                val textView=TextView(this)


        CoroutineScope(Dispatchers.IO).launch{

            val parsedInfo= getInfoFromEther.Download("https://guap.ru/rasp/?gr=6363")
            val createScheduleFromParsed=CreateScheduleFromParsed()
            try {
                schedule = createScheduleFromParsed.CreateSchedule(parsedInfo!!)

            }catch (e: Exception){
                Log.d("ex",e.message.toString())
            }
            withContext(Dispatchers.Main) {
                var txt=""
                for(i in 0 until parsedInfo!!.size){

                    Log.d("ew","$i) ${parsedInfo!![i]}")
                        //txt+="$i) ${parsedInfo!![i]}\n\n"
                }
                textView.text=""

                drawSchedule(schedule, linearLayout)
                linearLayout.invalidate()

                // Устанавливаем ScrollView как contentView
                setContentView(scrollView)


            }

        }




    }
    @SuppressLint("NewApi")
    private fun drawSchedule(schedule: ScheduleList, linearLayout: LinearLayout){
        val textView=TextView(this)
        // Создаем LinearLayout для содержимого ScrollView
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )


        linearLayout.addView(textView)
        // Получаем текущую дату
        var currentDate = now()

        // Форматируем дату
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE") // EEEE - полное название дня недели

        // Добавляем дни недели в список (например, на 30 дней вперед)

        for (i in 0 until 30) {
            // Создаем TextView для отображения дня недели и даты
            val textView = TextView(this)

            // Получаем день недели и форматируем дату
            //  val dayOfWeek = currentDate.dayOfWeek
            val weekNumb=((currentDate.minusDays(1)).format(DateTimeFormatter.ofPattern("w") ).toInt()+1)%2

            val formattedDate = currentDate.format(dateFormatter)
            val formattedDayOfWeek = currentDate.format(dayOfWeekFormatter)
            val dayOfWeekNumb =((currentDate.minusDays(2)).format(DateTimeFormatter.ofPattern("F") )).toInt()-1// EEEE - полное название дня недели
            val dayOfWeek=DaysOfWeek.values()[dayOfWeekNumb].dayOfWeek
            // Устанавливаем текст TextView
            textView.text = "$dayOfWeek $formattedDate"
            textView.textSize=20f

            // Устанавливаем отступы (опционально)
            textView.setPadding(100, 40, 16, 16)

            linearLayout.addView(textView, layoutParams)
            val linearLayoutParas=LinearLayout(this)
            linearLayoutParas.orientation=LinearLayout.VERTICAL
            // Добавляем TextView в LinearLayout
            for (j in 0..schedule.weeks[weekNumb].days[dayOfWeekNumb].paras.size-1){

                linearLayoutParas.addView(GenParaSchedule(schedule.weeks[weekNumb].days[dayOfWeekNumb].paras[j]), layoutParams)
            }
            val shape = GradientDrawable()
            shape.cornerRadius = 40f // радиус в dp
            shape.setColor(Color.argb(255,15,15,15)) // устанавливаем цвет фона

            // Установите фон для LinearLayout
            linearLayoutParas.background = shape
            linearLayout.setPadding(20,0,20,0)

            linearLayout.addView(linearLayoutParas,layoutParams)
            // Переходим к следующему дню
            currentDate = currentDate.plusDays(1)
        }



    }
    private fun GenParaSchedule(para: Para): LinearLayout{



        // Создаем TextView для номера урока
        val lessonNumberTextView = TextView(this)
        lessonNumberTextView.id = TextView.generateViewId()
        lessonNumberTextView.text = "${para.getNumbStr()}" // Пример: Номер урока 1
        lessonNumberTextView.setTextColor(Color.argb(255,130, 130, 130))
        lessonNumberTextView.textSize=29f
        lessonNumberTextView.setPadding(20,0,0,0)




        // Создаем TextView для времени начала занятия
        val timeStartTextView = TextView(this)


        timeStartTextView.text = para.getStartTime() // Пример текста
        timeStartTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white)) // Цвет текста
        //timeStartTextView.setPadding(8) // Отступы

        // Создаем TextView для времени  окончания занятия
        val timeEndTextView = TextView(this)


        timeEndTextView.text = para.getEndTime() // Пример текста
        timeEndTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white)) // Цвет текста

        // Создаем TextView для типа занятия
        val typeSubjectTextView = TextView(this)

        typeSubjectTextView.text = para.typeOfSubject // Пример текста
        when(para.typeOfSubject){
            TypeOfSubject.Lab.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,66, 151, 212))
            TypeOfSubject.Lek.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,122, 66, 212))
            TypeOfSubject.Pra.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,212, 163, 66))
        }

        // Создаем TextView для названия предмета
        val subjectTextView = TextView(this)

        subjectTextView.text = para.sub // Пример текста
        subjectTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white))


        // Создаем TextView для имени преподавателя
        val teacherTextView = TextView(this)

        teacherTextView.text = para.prepod // Пример текста
        teacherTextView.setTextColor(Color.argb(255,97, 97, 97))


        // Создаем TextView для аудитории
        val classroomTextView = TextView(this)

        classroomTextView.text = para.classRoom // Пример текста
        classroomTextView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))



        val layoutParams = LinearLayout.LayoutParams(

            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,

        )
        val linearLayout=LinearLayout(this)
        val linearLayoutLeft=LinearLayout(this)
        val linearLayoutRight=LinearLayout(this)

        linearLayoutLeft.orientation=LinearLayout.VERTICAL
        linearLayoutLeft.addView(timeStartTextView)
        linearLayoutLeft.addView(lessonNumberTextView)
        linearLayoutLeft.addView(timeEndTextView)

        linearLayoutRight.orientation=LinearLayout.VERTICAL
        linearLayoutRight.addView(typeSubjectTextView)
        linearLayoutRight.addView(subjectTextView)
        linearLayoutRight.addView(classroomTextView)
        linearLayoutRight.addView(teacherTextView)


        linearLayoutLeft.setPadding(50,20,100,20,)
        linearLayoutRight.setPadding(0,20,0,20,)
        linearLayout.orientation=LinearLayout.HORIZONTAL
        linearLayout.addView(linearLayoutLeft,layoutParams)
        linearLayout.addView(linearLayoutRight,layoutParams)



        return linearLayout
    }
}

