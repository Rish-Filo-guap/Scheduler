package com.example.scheduler

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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем ScrollView
        val scrollView = ScrollView(this)

        // Создаем LinearLayout для содержимого ScrollView
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black)) //настройка цвета там, где у телефона часы, ну короч сверху
        linearLayout.setBackgroundColor(Color.BLACK) //настройка фона всей приложеньки
        // Параметры LinearLayout
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Получаем текущую дату
        var currentDate = LocalDate.now()

        // Форматируем дату
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE") // EEEE - полное название дня недели
        val schedule=ScheduleList()
        // Добавляем дни недели в список (например, на 30 дней вперед)
        for (i in 0 until 30) {
            // Создаем TextView для отображения дня недели и даты
            val textView = TextView(this)

            // Получаем день недели и форматируем дату
            val dayOfWeek = currentDate.dayOfWeek
            val weekNumb=((currentDate.minusDays(1)).format(DateTimeFormatter.ofPattern("w") ).toInt()+1)%2

            val formattedDate = currentDate.format(dateFormatter)
            val formattedDayOfWeek = currentDate.format(dayOfWeekFormatter)
            val dayOfWeekNumb =((currentDate.minusDays(2)).format(DateTimeFormatter.ofPattern("F") )).toInt()-1// EEEE - полное название дня недели

            // Устанавливаем текст TextView
            textView.text = "$formattedDayOfWeek $formattedDate"
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
            //linearLayout.clipToOutline = true; // Обязательно, если используете elevation
            linearLayout.addView(linearLayoutParas,layoutParams)
            // Переходим к следующему дню
                currentDate = currentDate.plusDays(1)
        }

        // Добавляем LinearLayout в ScrollView
        scrollView.addView(linearLayout)

        // Устанавливаем ScrollView как contentView
        setContentView(scrollView)
    }
    private fun GenParaSchedule(para: Para): LinearLayout{



        // Создаем TextView для номера урока
        val lessonNumberTextView = TextView(this)
        lessonNumberTextView.id = TextView.generateViewId()
        lessonNumberTextView.text = para.numb.toString() // Пример: Номер урока 1
        //lessonNumberTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        lessonNumberTextView.setTextColor(Color.argb(255,130, 130, 130))
        lessonNumberTextView.textSize=29f

        //lessonNumberTextView.setPadding(0,0,0,0)




        // Создаем TextView для времени начала занятия
        val timeStartTextView = TextView(this)


        timeStartTextView.text = para.GetStartTime() // Пример текста
        timeStartTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white)) // Цвет текста
        //timeStartTextView.setPadding(8) // Отступы

        // Создаем TextView для времени  окончания занятия
        val timeEndTextView = TextView(this)


        timeEndTextView.text = para.GetEndTime() // Пример текста
        timeEndTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white)) // Цвет текста
        //timeEndTextView.setPadding(8) // Отступы

        // Создаем TextView для типа занятия
        val typeSubjectTextView = TextView(this)

        typeSubjectTextView.text = para.typeOfSubject.typeSub // Пример текста
        when(para.typeOfSubject){
            TypeOfSubject.Lab -> typeSubjectTextView.setTextColor(Color.argb(255,66, 151, 212))
            TypeOfSubject.Lek -> typeSubjectTextView.setTextColor(Color.argb(255,122, 66, 212))
            TypeOfSubject.Pra -> typeSubjectTextView.setTextColor(Color.argb(255,212, 163, 66))
        }
       //typeSubjectTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_purple))

        // Создаем TextView для названия предмета
        val subjectTextView = TextView(this)

        subjectTextView.text = para.sub.subject // Пример текста
        subjectTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        //subjectTextView.setPadding(8)


        // Создаем TextView для имени преподавателя
        val teacherTextView = TextView(this)

        teacherTextView.text = para.prepod.prepod // Пример текста
        teacherTextView.setTextColor(Color.argb(255,97, 97, 97))
        //teacherTextView.setPadding(8)


        // Создаем TextView для аудитории
        val classroomTextView = TextView(this)

        classroomTextView.text = para.classRoom.classroom // Пример текста
        classroomTextView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
       // classroomTextView.setPadding(8)



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



        //linearLayout.setBackgroundColor(Color.GRAY)



        return linearLayout
    }
}

