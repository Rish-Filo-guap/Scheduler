package com.example.scheduler.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.scheduler.scheduleViews.SchedulePageFragment
import com.example.scheduler.R
import com.example.scheduler.scheduleProcessing.CreateScheduleFromParsed
import com.example.scheduler.forAll.SearchActivity
import com.example.scheduler.forAll.ShowBottomFragmentDialogSearch
import com.example.scheduler.scheduleViews.OptionPageFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate.now

class MainActivity : AppCompatActivity(), ShowBottomFragmentDialogSearch {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var searchBTN: ImageButton
    private lateinit var prefs: SharedPreferences

    private lateinit var schedulePageFragment: SchedulePageFragment
    private lateinit var secSchedulePageFragment: SchedulePageFragment
    private lateinit var optionPageFragment: OptionPageFragment
    private var currentDate: Int = 0

    private lateinit var groupNames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentDate = now().dayOfYear

        window.setStatusBarColor(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        ) //настройка цвета там, где у телефона часы, ну короч сверху

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab_layout)


        // Set up the adapter
        viewPagerAdapter = ViewPagerAdapter(this)
        prefs = getSharedPreferences("info", Context.MODE_PRIVATE)

        searchBTN = findViewById(R.id.main_search_button)
        searchBTN.setOnClickListener {
            val searchDialogFragment = SearchActivity(this)
            searchDialogFragment.show(supportFragmentManager, "searchActivity")
        }
        try {

            val fileInputStream: FileInputStream = openFileInput("schedule.txt")
            schedulePageFragment =
                SchedulePageFragment(
                    prefs.getString("maingroup", null),
                    CreateScheduleFromParsed().ReadSchedule(fileInputStream)
                )
            Log.d("ew", "file finded")
            Toast.makeText(this, "загружена локальная версия расписания", Toast.LENGTH_SHORT).show()
            //mainSchedulePageFragment.showMessageTypeSchedule(true)
        } catch (e: Exception) {
            schedulePageFragment =
                SchedulePageFragment(prefs.getString("maingroup", null), null)
            Log.d("ew", "file not finded")
            //Toast.makeText(this, "загружено версия расписания с сайта", Toast.LENGTH_LONG).show()
            //mainSchedulePageFragment.showMessageTypeSchedule(false)
        }

        // Add the fragments
        secSchedulePageFragment = SchedulePageFragment(prefs.getString("secgroup", null), null)
        optionPageFragment= OptionPageFragment()



        groupNames =
            arrayOf(prefs.getString("maingroup", ":(")!!, prefs.getString("secgroup", ":(")!!)
        viewPagerAdapter.addFragment(schedulePageFragment, groupNames[0].substringBefore(" "))
        viewPagerAdapter.addFragment(secSchedulePageFragment, groupNames[1].substringBefore(" "))
        viewPagerAdapter.addFragment(optionPageFragment, "опции")


        // Set the adapter to the ViewPager2
        viewPager.adapter = viewPagerAdapter

        // Link the TabLayout and ViewPager2 using TabLayoutMediator
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()
    }


    override fun groupChanged(newGroup: String) {
        val editor = prefs.edit()
        when (viewPager.currentItem) {
            0 -> {
                schedulePageFragment.groupChanged(newGroup)
                editor.putString("maingroup", newGroup).apply()
            }

            1 -> {
                secSchedulePageFragment.groupChanged(newGroup)
                editor.putString("secgroup", newGroup).apply()

            }
        }

        groupNames[viewPager.currentItem] = newGroup
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = groupNames[position].substringBefore(" ")
        }.attach()


    }

    override fun onPause() {
        super.onPause()
        val fileOutputStream: FileOutputStream =
            openFileOutput("schedule.txt", Context.MODE_PRIVATE)
        CreateScheduleFromParsed().SaveSchedule(
            fileOutputStream,
            schedulePageFragment.scheduleLayout.schedule
        )

    }

    override fun onRestart() {
        super.onRestart()
        var cd = now().dayOfYear
        if (cd != currentDate) schedulePageFragment.invalidateSchedule()

        Log.d("ew", "$cd $currentDate")

    }


}
