package com.example.scheduler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.scheduler.ScheduleProcessing.CreateScheduleFromParsed
import com.example.scheduler.ScheduleProcessing.DaysOfWeek
import com.example.scheduler.ScheduleProcessing.GetInfoFromEther
import com.example.scheduler.ScheduleProcessing.GrPrCl
import com.example.scheduler.ScheduleProcessing.Para
import com.example.scheduler.ScheduleProcessing.ScheduleList
import com.example.scheduler.ScheduleProcessing.TypeOfSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter

class ScheduleLayout(context: Context, val parent:ShowBottomFragmentDialogParaInfo) : LinearLayout(context) {
    lateinit var schedule:ScheduleList
    val layoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )
    
    init {
        orientation = VERTICAL
        setBackgroundColor(Color.BLACK)
    }
    fun downloadSchedule(scheduleList: ScheduleList?, group: String){
        if(scheduleList==null){
            downloadSchedule(group)
        }
        else{
            schedule=scheduleList
            drawSchedule(schedule)
            invalidate()

        }

        Log.d("ew","wwwwwwwww")
    }
    fun downloadSchedule(group:String?){
        schedule= ScheduleList()
        setBackgroundResource(R.color.paraBackGroundColor)
        invalidate()
        if(group==null){return}

        CoroutineScope(Dispatchers.IO).launch {
            var newGroup:String
            if(GrPrCl().groups.containsKey(group))
                newGroup=GrPrCl().groups.get(group)!!
            else
                newGroup=GrPrCl().prepods.get(group)!!
            //delay(1000)
            val parsedInfo = GetInfoFromEther().Download("https://guap.ru/rasp/?"+ newGroup)

            val createScheduleFromParsed = CreateScheduleFromParsed()
            try {
                schedule = createScheduleFromParsed.CreateSchedule(parsedInfo!!)

            } catch (e: Exception) {
                Log.d("exer", e.message.toString())
            }
            withContext(Dispatchers.Main) {
                drawSchedule(schedule)
                setBackgroundColor(Color.BLACK)
                invalidate()

                Log.d("ew","wwwwwwwww")

            }
        }
    }

    @SuppressLint("NewApi")
    private fun drawSchedule(schedule: ScheduleList){
        removeAllViews()
        val textView= TextView(context)
        // Создаем LinearLayout для содержимого ScrollView



        addView(textView)
        // Получаем текущую дату
        var currentDate = now()
        // Форматируем дату
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")



        // Добавляем дни недели в список (например, на 30 дней вперед)

        for (i in 0 until 30) {
            // Создаем TextView для отображения дня недели и даты
            val textView = TextView(context)

            // Получаем день недели и форматируем дату
            val weekNumb=((currentDate.minusDays(0)).format(DateTimeFormatter.ofPattern("w") ).toInt()+1)%2

            val formattedDate = currentDate.format(dateFormatter)

            val dayOfWeekNumb =((currentDate.minusDays(2)).format(DateTimeFormatter.ofPattern("F") )).toInt()-1
            val dayOfWeek= DaysOfWeek.values()[dayOfWeekNumb].dayOfWeek
            // Устанавливаем текст TextView
            textView.text = "$dayOfWeek $formattedDate"
            textView.textSize=20f

            // Устанавливаем отступы (опционально)
            textView.setPadding(100, 40, 16, 16)

            addView(textView, layoutParams)
            val linearLayoutParas=LinearLayout(context)
            linearLayoutParas.orientation=VERTICAL


            /*for (j in 0..schedule.weeks[weekNumb].days[dayOfWeekNumb].paras.size-1){
                linearLayoutParas.addView(GenParaSchedule(schedule.weeks[weekNumb].days[dayOfWeekNumb].paras[j]), layoutParams)
            }*/
            for (j in 0..schedule.days[dayOfWeekNumb].paras.size-1){
                val paraLinearLayout=GenParaSchedule(schedule.days[dayOfWeekNumb].paras[j], weekNumb)
                if(paraLinearLayout!=null)
                    linearLayoutParas.addView(paraLinearLayout, layoutParams)
            }
            val shape = GradientDrawable()
            shape.cornerRadius = 40f // радиус в dp
            shape.setColor(ContextCompat.getColor(context, R.color.test)) // устанавливаем цвет фона

            // Установите фон для LinearLayout
            //linearLayoutParas.background = shape
            setPadding(20,0,20,0)

            addView(linearLayoutParas,layoutParams)
            // Переходим к следующему дню
            currentDate = currentDate.plusDays(1)
        }



    }

    @SuppressLint("ResourceAsColor")
    private fun GenParaSchedule(para: Para, weekNumb:Int): LinearLayout?{
        if(para.weekType!=weekNumb+1 && para.weekType!=0) return null
        else{



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
        timeStartTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white)) // Цвет текста
        timeStartTextView.gravity=Gravity.CENTER
        // Создаем TextView для времени  окончания занятия
        val timeEndTextView = TextView(context)

        timeEndTextView.text = para.getEndTime() // Пример текста
        timeEndTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white)) // Цвет текста
        timeEndTextView.gravity= Gravity.CENTER
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
        subjectTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white))


        // Создаем TextView для имени преподавателя
        val teacherTextView = TextView(context)

        teacherTextView.text = para.prepod.substringBefore(",") // Пример текста
        teacherTextView.setTextColor(ContextCompat.getColor(context, R.color.groupPrepClass))
        teacherTextView.maxLines=1
        teacherTextView.ellipsize=TextUtils.TruncateAt.END

        val groupsTextView = TextView(context)

        groupsTextView.text = para.groups // Пример текста
        groupsTextView.setTextColor(ContextCompat.getColor(context, R.color.groupPrepClass))






        // Создаем TextView для аудитории
        val classroomTextView = TextView(context)
        var cls=para.classRoom.substringAfter("(")
        var temp=""
        Log.d("ar",cls)
        when(cls[0])
        {
            'Б'->temp="БМ"
            'Г'->temp="Гаст"
            'Л'->temp="Ленс"
        }
        classroomTextView.text = para.classRoom.substringBefore(" ")+" "+temp // Пример текста
        classroomTextView.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))

        val layoutParams = LayoutParams(

            LayoutParams.WRAP_CONTENT,
            LayoutParams.MATCH_PARENT,

            )
        val linearLayout=LinearLayout(context)
        linearLayout.setOnClickListener {
            if(para.numb!=8)
                parent.showParaInfo(para)
            Log.d("ew", para.sub)
        }
        linearLayout.setOnLongClickListener {
            if(para.numb!=8)
                parent.editParaInfo(para)
            Log.d("ew", para.sub)
            true
        }






        val linearLayoutLeft=LinearLayout(context)
        val linearLayoutRight=LinearLayout(context)

        linearLayoutLeft.orientation=VERTICAL
        linearLayoutLeft.addView(timeStartTextView)
        linearLayoutLeft.addView(lessonNumberTextView)
        linearLayoutLeft.addView(timeEndTextView)

        linearLayoutLeft.gravity=Gravity.CENTER

        linearLayoutRight.orientation=VERTICAL
        linearLayoutRight.addView(typeSubjectTextView)
        linearLayoutRight.addView(subjectTextView)
        linearLayoutRight.addView(classroomTextView)
        linearLayoutRight.addView(teacherTextView)
        linearLayoutRight.addView(groupsTextView)




        linearLayoutLeft.setPadding(50,10,50,10)
        linearLayoutRight.setPadding(50,10,0,10)


        linearLayout.orientation=HORIZONTAL
        linearLayout.addView(linearLayoutLeft,layoutParams)

        linearLayout.addView(linearLayoutRight,layoutParams)
            when(weekNumb){
                0->linearLayout.setBackgroundResource(R.drawable.background_for_down_para)
                1->linearLayout.setBackgroundResource(R.drawable.background_for_up_para)
            }




        return linearLayout
        }
    }
}
