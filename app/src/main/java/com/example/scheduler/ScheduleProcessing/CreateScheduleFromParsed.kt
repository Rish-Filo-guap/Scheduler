package com.example.scheduler.ScheduleProcessing

import android.content.Context
import android.util.Log
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.FrameLayout
import android.content.SharedPreferences
import android.graphics.Color
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader

class CreateScheduleFromParsed {
    fun SaveSchedule(fileOutputStream: FileOutputStream, schedule:ScheduleList){
        try {
            //val fileOutputStream: FileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            val bufferedWriter = BufferedWriter(outputStreamWriter)
            for (i in 0..schedule.days.size-1) {
                bufferedWriter.write("h "+DaysOfWeek.values().get(i).dayOfWeek)
                bufferedWriter.newLine()
                for (para in schedule.days[i].paras) {
                    if(para.numb!=8){

                    bufferedWriter.write("d "+para.numb)
                    bufferedWriter.newLine()
                    if(para.weekType==1){
                        bufferedWriter.write("d "+'▼')
                        bufferedWriter.newLine()
                    }
                    if(para.weekType==2){
                        bufferedWriter.write("d "+'▲')
                        bufferedWriter.newLine()
                    }
                    bufferedWriter.write("d "+para.typeOfSubject)
                    bufferedWriter.newLine()

                    bufferedWriter.write("d "+para.sub)
                    bufferedWriter.newLine()

                    bufferedWriter.write("d ауд. "+para.classRoom)
                    bufferedWriter.write(" преп: "+para.prepod)
                    bufferedWriter.write(" гр: "+para.groups)
                    bufferedWriter.newLine()
                    }


                }
            }
            //bufferedWriter.write("d Показать расписание Очистить")
           // bufferedWriter.newLine()
            bufferedWriter.close()
            outputStreamWriter.close()
            fileOutputStream.close()
            Log.d("ew","Массив успешно сохранен в файл")
        } catch (e: IOException) {
            Log.d("ew","Ошибка при сохранении массива в файл: ${e.message}")
            e.printStackTrace()
        }

    }
    fun ReadSchedule(fileInputStream: FileInputStream):ScheduleList?{
        try{
            Log.d("ew", "file opened to read")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val list=ArrayList<String>()
            var line:String?=bufferedReader.readLine()
            while (line!=null){
                list.add(line)
                line=bufferedReader.readLine()
            }
            bufferedReader.close()
            inputStreamReader.close()
            fileInputStream.close()

            val res =CreateSchedule(list)
            Log.d("ew","Массив успешно загружен из файла")
           return res
        } catch (e: FileNotFoundException) {
            Log.d("ew","Файл не найден. Будет создан новый массив.")
            // Это нормально при первом запуске приложения
        } catch (e: IOException) {
            Log.d("ew","Ошибка при загрузке массива из файла: ${e.message}")
            e.printStackTrace()
        }
        return null
    }
    fun CreateSchedule(list: ArrayList<String>): ScheduleList {

        var scheduleList= ScheduleList()
        var hCounter=0
        var i=0;
        for(l in list) Log.d("ew", l)
        while (hCounter<2){
            if(list[i][0]=='h'){
                hCounter++
                if(list[i][3]!='н'){
                    i++
                    break
                }
            }

            i++

        }
        i--
        while(i<list.size-1){
                val selectedDay=getNumbDayFromString(list[i])
            //Log.d("ew","aaaaa "+i+" "+selectedDay)
            i++
            while (list[i][2].toInt()<='9'.toInt() && list[i][2].toInt()>='0'.toInt())
             {

                var weekType = 0
                if (list[i + 1][2] == '▼') weekType = 1
                if (list[i + 1][2] == '▲') weekType = 2


                val selectedPara = list[i][2].toString().toInt()
                var paraListString = ArrayList<String>()
                var para2ListString = ArrayList<String>()

                if(weekType<2){
                    paraListString.add(list[i + 1+weekType])
                    paraListString.add(list[i + 2+weekType])
                    paraListString.add(list[i + 3+weekType])
                    val para = getParaFromLines(paraListString, selectedPara,weekType)
                    scheduleList.days[selectedDay].addPara(para)
                    i+=4+weekType
                }
                else
                 {
                    para2ListString.add(list[i + 2])
                    para2ListString.add(list[i + 3])
                    para2ListString.add(list[i + 4])

                    val para2 = getParaFromLines(para2ListString, selectedPara, weekType)
                    scheduleList.days[selectedDay].addPara(para2)


                    if ((i+5<list.size-1) && (list[i+5][2].toInt()>'9'.toInt() || list[i+5][2].toInt()<'0'.toInt() ) && getNumbDayFromString(list[i+5])==-1) {
                        //Log.d("ew","e "+(i+5))
                        paraListString.add(list[i + 6])
                        paraListString.add(list[i + 7])
                        paraListString.add(list[i + 8])
                        val para = getParaFromLines(paraListString, selectedPara,1)
                        scheduleList.days[selectedDay].addPara(para)

                        i += 4
                    }



                    i += 5
                }


            }

        }
        return scheduleList



    }
    fun getParaFromLines(list: ArrayList<String>,selectedPara:Int, weekType:Int): Para {

        var classPrepGr=parseClassPrepGr(list[2])
        var subject=list[1].substring(2,list[1].length)

        var typeOfSub=parseTypeSub(list[0])

        //Log.d("ew",selectedPara.toString())
        return Para(subject, classPrepGr[1], classPrepGr[0], typeOfSub, classPrepGr[2],weekType, selectedPara.toString().toInt())
    }
    fun getNumbDayFromString(str:String):Int{
        when(str){
            "h "+ DaysOfWeek.Mon.dayOfWeek -> return 0
            "h "+ DaysOfWeek.Tue.dayOfWeek -> return 1
            "h "+ DaysOfWeek.Wed.dayOfWeek -> return 2
            "h "+ DaysOfWeek.Thu.dayOfWeek -> return 3
            "h "+ DaysOfWeek.Fri.dayOfWeek -> return 4
            "h "+ DaysOfWeek.Sat.dayOfWeek -> return 5
            "h "+ DaysOfWeek.Sun.dayOfWeek -> return 6
        }
        return -1
    }
    fun parseTypeSub(str:String):String{
        when(str[3]){
            'р' -> return TypeOfSubject.Pra.typeSub
            'е' -> return TypeOfSubject.Lek.typeSub
            'а' -> return TypeOfSubject.Lab.typeSub
            'у' -> return TypeOfSubject.Kyr.typeSub
        }
        return TypeOfSubject.No.typeSub
    }
    fun parseClassPrepGr(str:String):ArrayList<String>{
        var parsedList=ArrayList<String>()
        var classStart=0
        var prepStart=0
        var grStart=0
        for (i in 0 until str.length-1){
            if(str[i]=='.' && classStart==0){
                   classStart=i
            }
            if(str.substring(i,i+2)=="п:"){
               prepStart=i
            }
            if(str.substring(i,i+2)=="р:"){
                grStart=i
            }
           // Log.d("sub", str.substring(i,i+2))
        }

        if(prepStart!=0){
            var tmp=str.substring(classStart+2,prepStart-4)
            if(tmp.contains('—'))
                tmp=tmp.substringBefore(" —")
            parsedList.add(tmp)
            parsedList.add(str.substring(prepStart+3,grStart-2))
            parsedList.add(str.substring(grStart+3,str.length))


        }else{
            var tmp=str.substring(classStart+2,grStart-2)
           if(tmp.contains('—'))
                tmp=tmp.substringBefore(" —")
            parsedList.add(tmp)

           parsedList.add(tmp)
           parsedList.add("null")
           parsedList.add(str.substring(grStart+3,str.length))


        }
        return parsedList
    }
}
