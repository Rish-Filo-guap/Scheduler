package com.example.scheduler.scheduleViews

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.scheduler.R
import com.example.scheduler.scheduleProcessing.Para
import com.example.scheduler.scheduleProcessing.TypeOfSubject
import com.example.scheduler.forAll.ShowBottomFragmentDialogParaInfo

class ParaLinearLayoutCreator(private val context: Context, para:Para,val parent: ShowBottomFragmentDialogParaInfo) {

    private var linearLayout: LinearLayout? = null
    private var timeStartTextView: TextView? = null
    private var timeEndTextView: TextView? = null
    private var lessonNumbTextView: TextView? = null
    private var typeSubjectTextView: TextView? = null
    private var subjectTextView: TextView? = null
    private var classroomTextView: TextView? = null
    private var teacherTextView: TextView? = null
    private var groupTextView: TextView? = null




    init {
        // Получаем LayoutInflater из Context
        val inflater = LayoutInflater.from(context)

        // Раздуваем XML-файл в LinearLayout
        linearLayout = inflater.inflate(R.layout.para_linear_layout, null) as LinearLayout


        timeStartTextView=linearLayout?.findViewById(R.id.time_start_text_view)
        lessonNumbTextView=linearLayout?.findViewById(R.id.lesson_numb_text_view)
        timeEndTextView=linearLayout?.findViewById(R.id.time_end_text_view)
        typeSubjectTextView=linearLayout?.findViewById(R.id.type_subject_text_view)
        subjectTextView=linearLayout?.findViewById(R.id.subject_text_view)
        classroomTextView=linearLayout?.findViewById(R.id.classroom_text_view)
        teacherTextView=linearLayout?.findViewById(R.id.teacher_text_view)
        groupTextView=linearLayout?.findViewById(R.id.group_text_view)

        var cls=para.classRoom.substringAfter("(")
        var building=""

        when(cls[0])
        {
            'Б'->building="БМ"
            'Г'->building="Гаст"
            'Л'->building="Ленс"

        }
        
        

        timeStartTextView?.text=para.getStartTime()
        lessonNumbTextView?.text=para.getNumbStr()
        timeEndTextView?.text=para.getEndTime()
        typeSubjectTextView?.text=para.typeOfSubject
        subjectTextView?.text=para.sub
        classroomTextView?.text = para.classRoom.substringBefore(" ")+" "+building // Пример текста
        teacherTextView?.text=para.prepod.substringBefore(",")
        groupTextView?.text=para.groups

        when(para.weekType){

            0->linearLayout?.setBackgroundResource(R.drawable.background_for_every_para)
            1->linearLayout?.setBackgroundResource(R.drawable.background_for_down_para)
            2->linearLayout?.setBackgroundResource(R.drawable.background_for_up_para)
        }
        when(para.typeOfSubject){
            TypeOfSubject.Lab.typeSub -> typeSubjectTextView?.setTextColor(ContextCompat.getColor(context, R.color.labTextColor))
            TypeOfSubject.Lek.typeSub -> typeSubjectTextView?.setTextColor(ContextCompat.getColor(context, R.color.lekTextColor))
            TypeOfSubject.Pra.typeSub -> typeSubjectTextView?.setTextColor(ContextCompat.getColor(context, R.color.praTextColor))
        }
        linearLayout?.setOnClickListener {
            if(para.numb!=8)
                parent.showParaInfo(para)

        }
        linearLayout?.setOnLongClickListener {
            if(para.numb!=8)
                parent.editParaInfo(para)

            true
        }


        // Находим дочерние элементы внутри LinearLayout
        //textView = linearLayout?.findViewById(R.id.textView1)
        //editText = linearLayout?.findViewById(R.id.editText1)

        //  Можно выполнить дополнительные настройки элементов здесь
        //textView?.text = "Новый текст для TextView"
        //editText?.hint = "Новая подсказка для EditText"
    }

    // Функция для получения созданного LinearLayout
    fun getLinearLayout(): LinearLayout? {
        return linearLayout
    }

}
