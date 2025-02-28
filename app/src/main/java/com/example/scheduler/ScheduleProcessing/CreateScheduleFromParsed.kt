package com.example.scheduler.ScheduleProcessing

import android.util.Log
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader

class CreateScheduleFromParsed {
    fun SaveSchedule(fileOutputStream: FileOutputStream, schedule:ScheduleList){
        try {

            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            val bufferedWriter = BufferedWriter(outputStreamWriter)

            if(schedule.days[7].paras.size>=1){

                bufferedWriter.write("h Вне")
                bufferedWriter.newLine()
                for(para in schedule.days[7].paras){

                    bufferedWriter.write("d ${para.typeOfSubject} ${para.weekType}${para.dayOfWeek}${para.numb}" )
                    bufferedWriter.newLine()

                    bufferedWriter.write("d "+para.sub)
                    bufferedWriter.newLine()
                    if (para.classRoom!="null")
                        bufferedWriter.write("d ауд. "+para.classRoom)
                    if (para.prepod!="null")
                        bufferedWriter.write(" преп: "+para.prepod)
                    if (para.groups!="null")
                        bufferedWriter.write(" гр: "+para.groups)
                    bufferedWriter.newLine()
                }
            }
            for (i in 0..5) {



                    bufferedWriter.write("h "+DaysOfWeek.values().get(i).dayOfWeek)
                    bufferedWriter.newLine()
                    var j=0
                    for (para in schedule.days[i].paras) {

                            if((j>0 && schedule.days[i].paras[j-1].numb!=para.numb) || j==0){

                            bufferedWriter.write("d " + para.numb)
                            bufferedWriter.newLine()
                            }
                            if (para.weekType == 1) {
                                bufferedWriter.write("d " + '▼')
                                bufferedWriter.newLine()
                            }
                            if (para.weekType == 2) {
                                bufferedWriter.write("d " + '▲')
                                bufferedWriter.newLine()
                            }
                            bufferedWriter.write("d " + para.typeOfSubject)
                            bufferedWriter.newLine()

                            bufferedWriter.write("d " + para.sub)
                            bufferedWriter.newLine()

                            if (para.classRoom!="null")
                                bufferedWriter.write("d ауд. "+para.classRoom)
                            if (para.prepod!="null")
                                bufferedWriter.write(" преп: "+para.prepod)
                            if (para.groups!="null")
                                bufferedWriter.write(" гр: "+para.groups)
                            bufferedWriter.newLine()
                            j++

                    }

            }
            bufferedWriter.write("d Показать расписание Очистить")
            bufferedWriter.newLine()
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
        var j=1
        if(hCounter==2) {

            //вне сетки
            while (j<i){
                var paraListString = ArrayList<String>()
                paraListString.add(list[j])
                paraListString.add(list[j+1])
                paraListString.add(list[j+2])

                if (list[j][list[j].length-1].isDigit()){


                    val numb=list[j][list[j].length-1].digitToInt()
                    val dayOfWeek=list[j][list[j].length-2].digitToInt()
                    val weekType=list[j][list[j].length-3].digitToInt()
                    val para = getParaFromLines(paraListString, numb, weekType)
                    scheduleList.days[7].addPara(para,dayOfWeek)
                }else{
                    val para = getParaFromLines(paraListString, 1, 0)
                    scheduleList.days[7].addPara(para,0)
                }


                j+=3

            }
        }
        //в сетке
        while(i<list.size-1){
                val selectedDay=getNumbDayFromString(list[i])

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

        var typeOfSub=parseTypeSub(list[0])
        var subject=list[1].substring(2,list[1].length)
        var classPrepGr=parseClassPrepGr(list[2])



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
        if(str.contains("ауд.")) {
            var clasFtmp = str.substringAfter("ауд. ").substringBefore(" гр")
            var clasStmp = str.substringAfter("ауд. ").substringBefore(" преп")
            var classname = ""

            if (clasFtmp.length <= clasStmp.length)
                classname = clasFtmp
            else
                classname = clasStmp


            parsedList.add(classname)
        }else parsedList.add("null")



        if(str.contains("преп")) {
            var clasFtmp = str.substringAfter("преп: ").substringBefore(" гр")
            var clasStmp = str.substringAfter("преп: ")
            var classname = ""

            if (clasFtmp.length <= clasStmp.length)
                classname = clasFtmp
            else
                classname = clasStmp


            parsedList.add(classname)
        }else parsedList.add("null")

        if(str.contains("гр:")) {


            parsedList.add(str.substringAfter("гр: "))
        }else parsedList.add("null")

        return parsedList
    }
}
