package com.example.scheduler

import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), SelectGroup {

    var group:String="6431"
    lateinit var scheduleLayout:ScheduleLayout

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black)) //настройка цвета там, где у телефона часы, ну короч сверху
        scheduleLayout= ScheduleLayout(this)
        scheduleLayout.downloadSchedule(group)

        // Создаем ScrollView
        val scrollView = ScrollView(this)
        val mainLinearLayout=LinearLayout(this)
        setContentView(mainLinearLayout)

        val btn=Button(this)
        btn.text="search"

        mainLinearLayout.orientation = LinearLayout.VERTICAL
        mainLinearLayout.addView(btn)
        mainLinearLayout.addView(scrollView)

        btn.setOnClickListener{
            val searchDialogFragment = SearchActivity()

            searchDialogFragment.show(supportFragmentManager, "searchActivity")
        }

        scrollView.addView(scheduleLayout)

    }

    override fun groupChanged(newGroup: String) {

        group=Groups().groups.get(newGroup).toString()
        scheduleLayout.downloadSchedule(group)

    }

}

