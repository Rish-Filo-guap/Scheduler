package com.example.scheduler

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup.MarginLayoutParams
import android.view.Gravity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.VERTICAL
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.example.scheduler.ScheduleProcessing.Para
import com.example.scheduler.ScheduleProcessing.TypeOfSubject


class ParaInfo(val para: Para) : BottomSheetDialogFragment() {

 private lateinit var linearLayout: LinearLayout



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_para_info_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)

        linearLayout=view.findViewById(R.id.parainfo_linearLayout)
        GenParaSchedule(para,view)
        linearLayout.invalidate()

       // linearLayout.addView(GenParaSchedule(para))

    }
    @SuppressLint("ResourceAsColor")
    private fun GenParaSchedule(para: Para, view: View){
        val weekRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupWeekParity)
        val numbRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupNumbPara)
        val dayRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupDayPara)
        val idesForWeek= arrayOf(R.id.radioButtonAllWeek, R.id.radioButtonDownWeek, R.id.radioButtonUpWeek)
        val idesForNumb= arrayOf(
            R.id.radioButtonPara1,
            R.id.radioButtonPara2,
            R.id.radioButtonPara3,
            R.id.radioButtonPara4,
            R.id.radioButtonPara5,
            R.id.radioButtonPara6,
            R.id.radioButtonPara7,
            )
        val idesForDays= arrayOf(
            R.id.radioButtonDayPara1,
            R.id.radioButtonDayPara2,
            R.id.radioButtonDayPara3,
            R.id.radioButtonDayPara4,
            R.id.radioButtonDayPara5,
            R.id.radioButtonDayPara6,

        )
        weekRadioGroup.check(idesForWeek[para.weekType])
        numbRadioGroup.check(idesForNumb[para.numb-1])
        dayRadioGroup.check(idesForDays[para.dayOfWeek])



    }






}