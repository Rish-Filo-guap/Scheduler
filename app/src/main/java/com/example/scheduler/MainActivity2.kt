package com.example.scheduler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.scheduler.ui.main.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity2 : AppCompatActivity() {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab_layout)

        // Set up the adapter
        viewPagerAdapter = ViewPagerAdapter(this)

        // Add the fragments
        viewPagerAdapter.addFragment(FirstPageFragment(), "Page 1")
        viewPagerAdapter.addFragment(FirstPageFragment(), "Page 2")
        viewPagerAdapter.addFragment(FirstPageFragment(), "Page 3")

        // Set the adapter to the ViewPager2
        viewPager.adapter = viewPagerAdapter

        // Link the TabLayout and ViewPager2 using TabLayoutMediator
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()
    }
}