package com.example.scheduler

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.scheduler.ScheduleProcessing.Groups
import com.example.scheduler.ui.main.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(),GroupSaving {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black)) //настройка цвета там, где у телефона часы, ну короч сверху

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab_layout)

        // Set up the adapter
        viewPagerAdapter = ViewPagerAdapter(this)
        prefs = getSharedPreferences("info", Context.MODE_PRIVATE)

        // Add the fragments

        viewPagerAdapter.addFragment(MainSchedulePageFragment(loadGroup(),this), "Мое расписание")
        //viewPagerAdapter.addFragment(MainSchedulePageFragment(), "Page 2")
       // viewPagerAdapter.addFragment(FirstPageFragment(), "Page 2")
       // viewPagerAdapter.addFragment(FirstPageFragment(), "Page 3")

        // Set the adapter to the ViewPager2
        viewPager.adapter = viewPagerAdapter

        // Link the TabLayout and ViewPager2 using TabLayoutMediator
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()
    }
override fun saveGroup(group:String){//сохраняем все данные
    val editor = prefs.edit()
    editor.putString("group", group).apply()
}
private fun loadGroup(): String? {//загружаем все данные

   return prefs.getString("group",null)
}
}
