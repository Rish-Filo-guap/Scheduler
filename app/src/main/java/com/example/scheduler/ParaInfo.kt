package com.example.scheduler

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.scheduler.ScheduleProcessing.Para


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

        val textView=TextView(view.context)
        textView.text=para.groups
        linearLayout.addView(textView)

    }







}