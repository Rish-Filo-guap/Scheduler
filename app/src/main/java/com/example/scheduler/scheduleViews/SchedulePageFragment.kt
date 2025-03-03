package com.example.scheduler.scheduleViews

//import androidx.fragment.app.viewModels

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.example.scheduler.forAll.InvaludateSchedule
import com.example.scheduler.R
import com.example.scheduler.scheduleProcessing.Para
import com.example.scheduler.scheduleProcessing.ScheduleList
import com.example.scheduler.forAll.ShowBottomFragmentDialogParaInfo
import com.example.scheduler.paraInteraction.ParaEdit
import com.example.scheduler.paraInteraction.ParaInfo

class SchedulePageFragment(var group: String?, var scheduleList: ScheduleList?) : Fragment(),
    ShowBottomFragmentDialogParaInfo, InvaludateSchedule {


    lateinit var scheduleLayout: ScheduleLayout
    lateinit var mainLinearLayout: LinearLayout
    lateinit var scrollView: ScrollView


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

        scrollView = ScrollView(view.context)
        scheduleLayout = ScheduleLayout(view.context, this)
        if (group != null) {

            if (scheduleList == null) {
                scheduleLayout.downloadSchedule(group!!)
                Log.d("ew", "sssssss")
            } else {
                scheduleLayout.downloadSchedule(scheduleList, group!!)
            }
        }

        mainLinearLayout = view.findViewById(R.id.main_schedule_page_linearLayout)

        mainLinearLayout.addView(scrollView)

        scrollView.addView(scheduleLayout)
    }

    fun groupChanged(newGroup: String) {

        scheduleLayout.downloadSchedule(newGroup)
    }


    override fun showParaInfo(para: Para) {

        val paradialog = ParaInfo(para)
        paradialog.show(childFragmentManager, "ParaInfo")
    }

    override fun editParaInfo(para: Para) {

        val paradialog = ParaEdit(para, this)
        paradialog.show(childFragmentManager, "ParaEdit")

    }

    override fun invalidateSchedule() {
        mainLinearLayout.post {

            scheduleLayout.drawSchedule()
        }
        Log.d("rar", "invalid")
    }
}