package com.example.scheduler

import android.graphics.Color
import android.os.Build
import android.os.Bundle
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

        // Параметры LinearLayout
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Получаем текущую дату
        var currentDate = LocalDate.now()

        // Форматируем дату
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE") // EEEE - полное название дня недели

        // Добавляем дни недели в список (например, на 30 дней вперед)
        for (i in 0 until 30) {
            // Создаем TextView для отображения дня недели и даты
            val textView = TextView(this)

            // Получаем день недели и форматируем дату
            val dayOfWeek = currentDate.dayOfWeek
            val formattedDate = currentDate.format(dateFormatter)
            val formattedDayOfWeek = currentDate.format(dayOfWeekFormatter)
            val dayOfWeekNumb =(currentDate.minusDays(2)).format(DateTimeFormatter.ofPattern("F") )// EEEE - полное название дня недели

            // Устанавливаем текст TextView
            textView.text = "$formattedDayOfWeek, $formattedDate, $dayOfWeekNumb"


            // Устанавливаем отступы (опционально)
            textView.setPadding(100, 30, 16, 16)

            linearLayout.addView(textView, layoutParams)

            // Добавляем TextView в LinearLayout
            for (i in 0..3){

            linearLayout.addView(GenDaySchedule(), layoutParams)
            }

            // Переходим к следующему дню
                currentDate = currentDate.plusDays(1)
        }

        // Добавляем LinearLayout в ScrollView
        scrollView.addView(linearLayout)

        // Устанавливаем ScrollView как contentView
        setContentView(scrollView)
    }
    private fun GenDaySchedule(): LinearLayout{



        // Создаем TextView для номера урока
        val lessonNumberTextView = TextView(this)
        lessonNumberTextView.id = TextView.generateViewId()
        lessonNumberTextView.text = "1" // Пример: Номер урока 1
        //lessonNumberTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        lessonNumberTextView.setTextColor(Color.BLUE)
        lessonNumberTextView.textSize=20f

        //lessonNumberTextView.setPadding(0,0,0,0)




        // Создаем TextView для времени начала занятия
        val timeStartTextView = TextView(this)

        timeStartTextView.id = TextView.generateViewId() // Генерируем уникальный ID
        timeStartTextView.text = "10:00" // Пример текста
        timeStartTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white)) // Цвет текста
        //timeStartTextView.setPadding(8) // Отступы

        // Создаем TextView для времени  окончания занятия
        val timeEndTextView = TextView(this)

        timeEndTextView.id = TextView.generateViewId() // Генерируем уникальный ID
        timeEndTextView.text = "11:30" // Пример текста
        timeEndTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white)) // Цвет текста
        //timeEndTextView.setPadding(8) // Отступы



        // Создаем TextView для названия предмета
        val subjectTextView = TextView(this)
        subjectTextView.id = TextView.generateViewId()
        subjectTextView.text = "Математический анализ" // Пример текста
        subjectTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_purple))
        //subjectTextView.setPadding(8)


        // Создаем TextView для имени преподавателя
        val teacherTextView = TextView(this)
        teacherTextView.id = TextView.generateViewId()
        teacherTextView.text = "Иванов И.И." // Пример текста
        teacherTextView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        //teacherTextView.setPadding(8)


        // Создаем TextView для аудитории
        val classroomTextView = TextView(this)
        classroomTextView.id = TextView.generateViewId()
        classroomTextView.text = "Ауд. 201" // Пример текста
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
        linearLayoutRight.addView(subjectTextView)
        linearLayoutRight.addView(classroomTextView)
        linearLayoutRight.addView(teacherTextView)


        linearLayoutLeft.setPadding(30,40,100,0,)
        linearLayoutRight.setPadding(0,40,0,0,)
        linearLayout.orientation=LinearLayout.HORIZONTAL
        linearLayout.addView(linearLayoutLeft,layoutParams)
        linearLayout.addView(linearLayoutRight,layoutParams)


        return linearLayout
    }
}

