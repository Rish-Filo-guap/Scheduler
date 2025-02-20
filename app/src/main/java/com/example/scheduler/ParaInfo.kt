package com.example.scheduler

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.core.content.ContextCompat
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


        linearLayout.addView(GenParaSchedule(para))

    }
    @SuppressLint("ResourceAsColor")
    private fun GenParaSchedule(para: Para): LinearLayout{


        // Создаем TextView для номера урока
        val lessonNumberTextView = TextView(context)

        lessonNumberTextView.id = TextView.generateViewId()
        lessonNumberTextView.text = "${para.getNumbStr()}" // Пример: Номер урока 1
        lessonNumberTextView.setTextColor(Color.argb(255,130, 130, 130))
        lessonNumberTextView.textSize=29f
        lessonNumberTextView.gravity=Gravity.CENTER


        // Создаем TextView для времени начала занятия
        val timeStartTextView = TextView(context)

        timeStartTextView.text = para.getStartTime() // Пример текста
        timeStartTextView.setTextColor(Color.WHITE) // Цвет текста
        timeStartTextView.gravity=Gravity.CENTER

        // Создаем TextView для времени  окончания занятия
        val timeEndTextView = TextView(context)

        timeEndTextView.text = para.getEndTime() // Пример текста
        timeEndTextView.setTextColor(Color.WHITE) // Цвет текста
        timeEndTextView.gravity=Gravity.CENTER

        // Создаем TextView для типа занятия
        val typeSubjectTextView = TextView(context)

        typeSubjectTextView.text = para.typeOfSubject // Пример текста
        when(para.typeOfSubject){
            TypeOfSubject.Lab.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,66, 151, 212))
            TypeOfSubject.Lek.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,122, 66, 212))
            TypeOfSubject.Pra.typeSub -> typeSubjectTextView.setTextColor(Color.argb(255,212, 163, 66))
        }

        // Создаем TextView для названия предмета
        val subjectTextView = TextView(context)

        subjectTextView.text = para.sub // Пример текста
        subjectTextView.setTextColor(Color.WHITE)


        // Создаем TextView для имени преподавателя
        val teacherTextView = TextView(context)

        teacherTextView.text = para.prepod // Пример текста
        teacherTextView.setTextColor(Color.WHITE)
        teacherTextView.maxLines=1
        teacherTextView.ellipsize= TextUtils.TruncateAt.END

        val groupsTextView = TextView(context)

        groupsTextView.text = para.groups // Пример текста
        groupsTextView.setTextColor(Color.WHITE)






        // Создаем TextView для аудитории
        val classroomTextView = TextView(context)

        classroomTextView.text = para.classRoom // Пример текста
        classroomTextView.setTextColor(Color.WHITE)

        val layoutParams = LayoutParams(

            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,

            )
        val linearLayout=LinearLayout(context)

        val linearLayoutLeft=LinearLayout(context)
        val linearLayoutRight=LinearLayout(context)

        linearLayoutLeft.orientation= VERTICAL
        linearLayoutLeft.addView(timeStartTextView)
        linearLayoutLeft.addView(lessonNumberTextView)
        linearLayoutLeft.addView(timeEndTextView)

        linearLayoutRight.orientation= VERTICAL
        linearLayoutRight.addView(typeSubjectTextView)
        linearLayoutRight.addView(subjectTextView)
        linearLayoutRight.addView(classroomTextView)
        linearLayoutRight.addView(teacherTextView)
        linearLayoutRight.addView(groupsTextView)



        linearLayoutLeft.setPadding(50,20,100,20,)
        linearLayoutRight.setPadding(0,20,0,20,)
        linearLayout.orientation= HORIZONTAL
        linearLayout.addView(linearLayoutLeft,layoutParams)
        linearLayout.addView(linearLayoutRight,layoutParams)
        linearLayout.post{
            linearLayoutLeft.gravity=Gravity.CENTER_HORIZONTAL

        }

        return linearLayout
    }






}