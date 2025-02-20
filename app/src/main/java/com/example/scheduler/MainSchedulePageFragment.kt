package com.example.scheduler

//import androidx.fragment.app.viewModels

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import com.example.scheduler.ScheduleProcessing.Groups
import com.example.scheduler.ScheduleProcessing.Para

class MainSchedulePageFragment(var group:String?, val parent:GroupSaving) : Fragment(), ShowBottomFragmentDialog {
    
    lateinit var scheduleLayout:ScheduleLayout


    //private val viewModel: FirstPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
        
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main_schedule_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scrollView = ScrollView(view.context)
        scheduleLayout= ScheduleLayout(view.context, this)
        if(group!=null){
            scheduleLayout.downloadSchedule(group!!)
        }

        var mainLinearLayout:LinearLayout= view.findViewById(R.id.main_schedule_page_linearLayout)

        val btn= Button(view.context)
        btn.text="search"

        mainLinearLayout.addView(btn)
        mainLinearLayout.addView(scrollView)
        btn.setOnClickListener{

            val searchDialogFragment = SearchActivity(this)
            searchDialogFragment.show(childFragmentManager, "searchActivity")
        }
        scrollView.addView(scheduleLayout)
    }
    override fun groupChanged(newGroup: String) {

        group= Groups().groups.get(newGroup).toString()
        scheduleLayout.downloadSchedule(group!!)
        parent.saveGroup(group!!)

    }

    override fun showParaInfo(para: Para) {
        val paradialog=ParaInfo(para)
        paradialog.show(childFragmentManager, "ParaInfo")
    }
}