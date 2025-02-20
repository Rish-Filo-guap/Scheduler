package com.example.scheduler

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.scheduler.ScheduleProcessing.Groups
import com.example.scheduler.ui.main.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), ShowBottomFragmentDialogSearch {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var searchBTN: ImageButton
    private lateinit var prefs: SharedPreferences
    private lateinit var mainSchedulePageFragment:MainSchedulePageFragment
    private lateinit var secSchedulePageFragment:MainSchedulePageFragment
    private val tabsNames=arrayOf("Мое расписание ", "Чужое расписание ")
    private lateinit var groupNames:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black)) //настройка цвета там, где у телефона часы, ну короч сверху

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab_layout)

        // Set up the adapter
        viewPagerAdapter = ViewPagerAdapter(this)
        prefs = getSharedPreferences("info", Context.MODE_PRIVATE)

        searchBTN=findViewById(R.id.main_search_button)
        searchBTN.setOnClickListener {
            val searchDialogFragment = SearchActivity(this)
            searchDialogFragment.show(supportFragmentManager, "searchActivity")
        }

        // Add the fragments
        mainSchedulePageFragment=MainSchedulePageFragment(prefs.getString("maingroup",null))
        secSchedulePageFragment=MainSchedulePageFragment(prefs.getString("secgroup",null))


        groupNames= arrayOf(prefs.getString("maingroup",":(")!!, prefs.getString("secgroup",":(")!! )
        viewPagerAdapter.addFragment(mainSchedulePageFragment, tabsNames[0]+groupNames[0])
        viewPagerAdapter.addFragment(secSchedulePageFragment, tabsNames[1]+groupNames[1])


        // Set the adapter to the ViewPager2
        viewPager.adapter = viewPagerAdapter

        // Link the TabLayout and ViewPager2 using TabLayoutMediator
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()
    }



    override fun groupChanged(newGroup: String) {
        val editor = prefs.edit()
        when(viewPager.currentItem){
            0->{
                mainSchedulePageFragment.groupChanged(newGroup)
                editor.putString("maingroup", newGroup).apply()
            }
            1->{
                secSchedulePageFragment.groupChanged(newGroup)
                editor.putString("secgroup", newGroup).apply()

            }
        }

        groupNames[viewPager.currentItem]=newGroup
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsNames[position]+groupNames[position]
        }.attach()



    }

}
