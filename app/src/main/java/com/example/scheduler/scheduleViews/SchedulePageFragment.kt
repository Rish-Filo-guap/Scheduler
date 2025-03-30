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
import androidx.core.view.children
import com.example.scheduler.forAll.InvaludateSchedule
import com.example.scheduler.R
import com.example.scheduler.forAll.ChangeTabByCode
import com.example.scheduler.scheduleProcessing.Para
import com.example.scheduler.scheduleProcessing.ScheduleList
import com.example.scheduler.forAll.ShowBottomFragmentDialogParaInfo
import com.example.scheduler.paraInteraction.ParaEdit
import com.example.scheduler.paraInteraction.ParaInfo
import java.io.FileInputStream

class SchedulePageFragment(var group: String?, var fileInputStream: FileInputStream?) : Fragment(),
    ShowBottomFragmentDialogParaInfo, InvaludateSchedule {


    lateinit var scheduleLayout: ScheduleLayout
    lateinit var mainLinearLayout: LinearLayout
    lateinit var scrollView: ScrollView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main_schedule_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainLinearLayout = view.findViewById(R.id.main_schedule_page_linearLayout)
        scrollView = ScrollView(view.context)
        scheduleLayout = ScheduleLayout(view.context, this)
        var isRIghtSchedule = true
        if (group != null) {

            if (fileInputStream == null) {
                try {

                    scheduleLayout.downloadSchedule(group)
                } catch (e: Exception) {
                    Log.d(
                        "SchedulePageFragment",
                        "Не получилось скачать (сайт) расписание: ${e.message}"
                    )
                    isRIghtSchedule = false
                }
            } else {
                try {
                    scheduleLayout.downloadSchedule(fileInputStream!!, group!!)
                } catch (e: Exception) {
                    Log.d(
                        "SchedulePageFragment",
                        "Не получилось загрузить (файл) расписание: ${e.message}"
                    )
                    isRIghtSchedule = false
                }
            }
            if (isRIghtSchedule) {

                mainLinearLayout.removeAllViews()
                mainLinearLayout.addView(scrollView)
                scrollView.addView(scheduleLayout)
            }
        } else {
            Log.d("ew", "noSchedule")

        }


    }

    fun groupChanged(newGroup: String) {

        clearSchedule()
        scheduleLayout.downloadSchedule(newGroup)
    }

    fun codeChanged(newCode: String, parent: ChangeTabByCode) {
        clearSchedule()
        scheduleLayout.downloadScheduleFromServ(newCode, parent)
    }

    private fun clearSchedule() {
        if (mainLinearLayout.childCount == 2) {
            mainLinearLayout.removeAllViews()
            mainLinearLayout.addView(scrollView)
            scrollView.addView(scheduleLayout)
        }
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