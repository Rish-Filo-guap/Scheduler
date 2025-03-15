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
import com.example.scheduler.forAll.ChangeTabByCode
import com.example.scheduler.scheduleProcessing.CreateScheduleFromParsed
import com.example.scheduler.forAll.SearchActivity
import com.example.scheduler.forAll.ShowBottomFragmentDialogSearch
import com.example.scheduler.forAll.GetPostSchedule
import com.example.scheduler.forAll.ServerRequest
import com.example.scheduler.scheduleProcessing.GrPrCl
import com.example.scheduler.scheduleViews.OptionPageFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.FileOutputStream
import java.time.LocalDate.now

class MainActivity : AppCompatActivity(), ShowBottomFragmentDialogSearch, GetPostSchedule, ChangeTabByCode {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var searchBTN: ImageButton
    private lateinit var prefs: SharedPreferences

    private lateinit var schedulePageFragment: SchedulePageFragment
    private lateinit var secSchedulePageFragment: SchedulePageFragment
    private lateinit var optionPageFragment: OptionPageFragment
    private var currentDate: Int = 0
    private var tabNeedChangeGroup = 0

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
            schedulePageFragment =
                SchedulePageFragment(
                    prefs.getString("maingroup", null),
                    openFileInput("schedule.txt")

                )
            Log.d("ew", "fir file finded")
            Toast.makeText(this, "загружена локальная версия расписания", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            schedulePageFragment =
                SchedulePageFragment(prefs.getString("maingroup", null), null)
            Log.d("ew", "fir file not finded")

        }

        try {
            secSchedulePageFragment =
                SchedulePageFragment(
                    prefs.getString("secgroup", null),
                    openFileInput("secschedule.txt")

                )
            Log.d("ew", "sec file finded")
            Toast.makeText(this, "загружена локальная версия расписания", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            secSchedulePageFragment =
                SchedulePageFragment(prefs.getString("secgroup", null), null)
            Log.d("ew", "sec file not finded")

        }


        optionPageFragment = OptionPageFragment(this)



        groupNames =
            arrayOf(
                prefs.getString("maingroup", ":(")!!,
                prefs.getString("secgroup", ":(")!!,
                "опции"
            )
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

    override fun changeGroup(newGroup: String) {
        val editor = prefs.edit()
        when (tabNeedChangeGroup) {
            0 -> {
                editor.putString("maingroup", newGroup).apply()
            }

            1 -> {
                editor.putString("secgroup", newGroup).apply()
            }


        }
            groupNames[tabNeedChangeGroup] = newGroup
            TabLayoutMediator (tabLayout, viewPager) { tab, position ->
                tab.text = groupNames[position].substringBefore(" ")
            }.attach()
    }

    override fun groupChanged(newGroup: String) {

        when (viewPager.currentItem) {
            0 -> {
                schedulePageFragment.groupChanged(newGroup)
                tabNeedChangeGroup = 0
            }

            1 -> {
                secSchedulePageFragment.groupChanged(newGroup)
                tabNeedChangeGroup = 1

            }
        }

        changeGroup(newGroup)
    }

    override fun codeChanged(newCode: String) {
        val editor = prefs.edit()
        var newGroup = ""
        when (viewPager.currentItem) {
            0 -> {
                schedulePageFragment.codeChanged(newCode, this)
                tabNeedChangeGroup = 0
            }

            1 -> {
                secSchedulePageFragment.codeChanged(newCode, this)
                tabNeedChangeGroup = 1
            }
        }


    }

    override fun getGroup(pageNumb: Int): String? {
        when (pageNumb) {
            0 -> {
                return prefs.getString("maingroup", null)
            }

            1 -> {
                return prefs.getString("secgroup", null)
            }
        }
        return null
    }

    override suspend fun postSchedule(url: String, pageNumb: Int) {
        when (pageNumb) {
            0 -> {
                val sched =
                    CreateScheduleFromParsed().SaveSchedule(schedulePageFragment.scheduleLayout.schedule)
                if (sched != null)
                    ServerRequest().postRequest(url, sched)
                else
                    Log.d("mainAct", "не удалось получить строку расписания")
            }

            1 -> {}

        }

    }

    override fun onPause() {
        super.onPause()
        val fileOutputStream: FileOutputStream =
            openFileOutput("schedule.txt", Context.MODE_PRIVATE)
        CreateScheduleFromParsed().SaveSchedule(
            fileOutputStream,
            schedulePageFragment.scheduleLayout.schedule
        )
        val secfileOutputStream: FileOutputStream =
            openFileOutput("secschedule.txt", Context.MODE_PRIVATE)
        CreateScheduleFromParsed().SaveSchedule(
            secfileOutputStream,
            secSchedulePageFragment.scheduleLayout.schedule
        )

    }

    override fun onRestart() {
        super.onRestart()
        var cd = now().dayOfYear
        if (cd != currentDate) schedulePageFragment.invalidateSchedule()

        Log.d("ew", "$cd $currentDate")

    }


}
