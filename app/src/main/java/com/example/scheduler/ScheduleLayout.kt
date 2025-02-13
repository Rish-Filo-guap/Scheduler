package com.example.scheduler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter

class ScheduleLayout(context: Context) : LinearLayout(context) {
    val layoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )
    
    init {
        orientation = VERTICAL
        setBackgroundColor(Color.BLACK)
    }
    fun downloadSchedule(group:String){
        var schedule=ScheduleList()

        CoroutineScope(Dispatchers.IO).launch {
            //delay(1000)
            val parsedInfo = GetInfoFromEther().Download("https://guap.ru/rasp/?gr="+group)

            val createScheduleFromParsed = CreateScheduleFromParsed()
            try {
                schedule = createScheduleFromParsed.CreateSchedule(parsedInfo!!)

            } catch (e: Exception) {
                Log.d("exer", e.message.toString())
            }
            withContext(Dispatchers.Main) {
                drawSchedule(schedule)
                invalidate()

                Log.d("ew","wwwwwwwww")

            }
        }
    }

    @SuppressLint("NewApi")
    private fun drawSchedule(schedule: ScheduleList){
        removeAllViews()
        val textView= TextView(context)
        // Создаем LinearLayout для содержимого ScrollView



        addView(textView)
        // Получаем текущую дату
        var currentDate = now()
        // Форматируем дату
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")



        // Добавляем дни недели в список (например, на 30 дней вперед)

        for (i in 0 until 30) {
            // Создаем TextView для отображения дня недели и даты
            val textView = TextView(context)

            // Получаем день недели и форматируем дату
            val weekNumb=((currentDate.minusDays(1)).format(DateTimeFormatter.ofPattern("w") ).toInt()+1)%2

            val formattedDate = currentDate.format(dateFormatter)

            val dayOfWeekNumb =((currentDate.minusDays(2)).format(DateTimeFormatter.ofPattern("F") )).toInt()-1
            val dayOfWeek=DaysOfWeek.values()[dayOfWeekNumb].dayOfWeek
            // Устанавливаем текст TextView
            textView.text = "$dayOfWeek $formattedDate"
            textView.textSize=20f

            // Устанавливаем отступы (опционально)
            textView.setPadding(100, 40, 16, 16)

            addView(textView, layoutParams)
            val linearLayoutParas=LinearLayout(context)
            linearLayoutParas.orientation=VERTICAL


            for (j in 0..schedule.weeks[weekNumb].days[dayOfWeekNumb].paras.size-1){
                linearLayoutParas.addView(GenParaSchedule(schedule.weeks[weekNumb].days[dayOfWeekNumb].paras[j]), layoutParams)
            }
            val shape = GradientDrawable()
            shape.cornerRadius = 40f // радиус в dp
            shape.setColor(Color.argb(255,15,15,15)) // устанавливаем цвет фона

            // Установите фон для LinearLayout
            linearLayoutParas.background = shape
            setPadding(20,0,20,0)

            addView(linearLayoutParas,layoutParams)
            // Переходим к следующему дню
            currentDate = currentDate.plusDays(1)
        }



    }
    private fun GenParaSchedule(para: Para): LinearLayout{

        // Создаем TextView для номера урока
        val lessonNumberTextView = TextView(context)
        lessonNumberTextView.id = TextView.generateViewId()
        lessonNumberTextView.text = "${para.getNumbStr()}" // Пример: Номер урока 1
        lessonNumberTextView.setTextColor(Color.argb(255,130, 130, 130))
        lessonNumberTextView.textSize=29f
        lessonNumberTextView.setPadding(20,0,0,0)

        // Создаем TextView для времени начала занятия
        val timeStartTextView = TextView(context)

        timeStartTextView.text = para.getStartTime() // Пример текста
        timeStartTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white)) // Цвет текста

        // Создаем TextView для времени  окончания занятия
        val timeEndTextView = TextView(context)

        timeEndTextView.text = para.getEndTime() // Пример текста
        timeEndTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white)) // Цвет текста

        // Создаем TextView для типа занятия
        val typeSubjectTextView = TextView(context)

        typeSubjectTextView.text = para.typeOfSubject // Пример текста
        when(para.typeOfSubject){
            TypeOfSubject.Lab.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,66, 151, 212))
            TypeOfSubject.Lek.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,122, 66, 212))
            TypeOfSubject.Pra.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,212, 163, 66))
        }

        // Создаем TextView для названия предмета
        val subjectTextView = TextView(context)

        subjectTextView.text = para.sub // Пример текста
        subjectTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white))


        // Создаем TextView для имени преподавателя
        val teacherTextView = TextView(context)

        teacherTextView.text = para.prepod // Пример текста
        teacherTextView.setTextColor(Color.argb(255,97, 97, 97))


        // Создаем TextView для аудитории
        val classroomTextView = TextView(context)

        classroomTextView.text = para.classRoom // Пример текста
        classroomTextView.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))

        val layoutParams = LayoutParams(

            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,

            )
        val linearLayout=LinearLayout(context)
        val linearLayoutLeft=LinearLayout(context)
        val linearLayoutRight=LinearLayout(context)

        linearLayoutLeft.orientation=VERTICAL
        linearLayoutLeft.addView(timeStartTextView)
        linearLayoutLeft.addView(lessonNumberTextView)
        linearLayoutLeft.addView(timeEndTextView)

        linearLayoutRight.orientation=VERTICAL
        linearLayoutRight.addView(typeSubjectTextView)
        linearLayoutRight.addView(subjectTextView)
        linearLayoutRight.addView(classroomTextView)
        linearLayoutRight.addView(teacherTextView)


        linearLayoutLeft.setPadding(50,20,100,20,)
        linearLayoutRight.setPadding(0,20,0,20,)
        linearLayout.orientation=HORIZONTAL
        linearLayout.addView(linearLayoutLeft,layoutParams)
        linearLayout.addView(linearLayoutRight,layoutParams)

        return linearLayout
    }
}