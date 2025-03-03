package com.example.scheduler.scheduleViews

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.EditText
import com.example.scheduler.R

class WeekendLinearLayoutCreator(private val context: Context) {

    private var linearLayout: LinearLayout? = null


    init {
        // Получаем LayoutInflater из Context
        val inflater = LayoutInflater.from(context)

        // Раздуваем XML-файл в LinearLayout
        linearLayout = inflater.inflate(R.layout.weekend_linear_layout, null) as LinearLayout


    }

    // Функция для получения созданного LinearLayout
    fun getLinearLayout(): LinearLayout? {
        return linearLayout
    }

    //  Пример функций для доступа к дочерним элементам

}
