package com.example.scheduler

//import androidx.fragment.app.viewModels

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.example.scheduler.ScheduleProcessing.Para
import com.example.scheduler.ScheduleProcessing.ScheduleList

class MainSchedulePageFragment(var group:String?, var scheduleList: ScheduleList?) : Fragment(), ShowBottomFragmentDialogParaInfo {

    
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

        if(scheduleList==null){
            scheduleLayout.downloadSchedule(group!!)
            Log.d("ew", "sssssss")
        }else{
            scheduleLayout.downloadSchedule(scheduleList,group!!)
        }
        }

        var mainLinearLayout:LinearLayout= view.findViewById(R.id.main_schedule_page_linearLayout)



       // mainLinearLayout.addView(btn)
        mainLinearLayout.addView(scrollView)

        scrollView.addView(scheduleLayout)
    }
    fun groupChanged(newGroup: String) {

        scheduleLayout.downloadSchedule(newGroup)
    }

    override fun showParaInfo(para: Para) {

        val paradialog=ParaInfo(para)
        paradialog.show(childFragmentManager, "ParaInfo")
    }
}