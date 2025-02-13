package com.example.scheduler

import android.util.Log

class CreateScheduleFromParsed {
    fun CreateSchedule(list: ArrayList<String>):ScheduleList{
        var scheduleList=ScheduleList()
        var hCounter=0
        var i=0;
        while (hCounter<2){
            if(list[i][0]=='h') hCounter++
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

                //Log.d("iii", i.toString()+"| "+selectedPara+"| "+selectedDay+"| "+weekType )
                if (weekType == 0) {
                    paraListString.add(list[i + 1])
                    paraListString.add(list[i + 2])
                    paraListString.add(list[i + 3])
                    val para = getParaFromLines(paraListString, selectedPara)
                    scheduleList.weeks[0].days[selectedDay].addPara(para)
                    scheduleList.weeks[1].days[selectedDay].addPara(para)
                    i += 4
                }
                if (weekType == 1) {
                    paraListString.add(list[i + 2])
                    paraListString.add(list[i + 3])
                    paraListString.add(list[i + 4])
                    val para = getParaFromLines(paraListString, selectedPara)
                    scheduleList.weeks[0].days[selectedDay].addPara(para)

                    i += 5
                }
                if (weekType == 2) {
                    para2ListString.add(list[i + 2])
                    para2ListString.add(list[i + 3])
                    para2ListString.add(list[i + 4])
                    val para2 = getParaFromLines(para2ListString, selectedPara)
                    scheduleList.weeks[1].days[selectedDay].addPara(para2)

                    if ((i+5<list.size-1) && (list[i+5][2].toInt()>'9'.toInt() || list[i+5][2].toInt()<'0'.toInt() ) && getNumbDayFromString(list[i+5])==-1) {
                        //Log.d("ew","e "+(i+5))
                        paraListString.add(list[i + 6])
                        paraListString.add(list[i + 7])
                        paraListString.add(list[i + 8])
                        val para = getParaFromLines(paraListString, selectedPara)
                        scheduleList.weeks[0].days[selectedDay].addPara(para)
                        i += 4
                    }



                    i += 5
                }


            }

        }
        return scheduleList



    }
    fun getParaFromLines(list: ArrayList<String>,selectedPara:Int):Para{

        var classPrepGr=parseClassPrepGr(list[2])
        var subject=list[1].substring(2,list[1].length)

        var typeOfSub=parseTypeSub(list[0])

        //Log.d("ew",selectedPara.toString())
        return Para(subject, classPrepGr[1], classPrepGr[0], typeOfSub, classPrepGr[2], selectedPara.toString().toInt())
    }
    fun getNumbDayFromString(str:String):Int{
        when(str){
            "h "+DaysOfWeek.Mon.dayOfWeek -> return 0
            "h "+DaysOfWeek.Tue.dayOfWeek -> return 1
            "h "+DaysOfWeek.Wed.dayOfWeek -> return 2
            "h "+DaysOfWeek.Thu.dayOfWeek -> return 3
            "h "+DaysOfWeek.Fri.dayOfWeek -> return 4
            "h "+DaysOfWeek.Sat.dayOfWeek -> return 5
            "h "+DaysOfWeek.Sun.dayOfWeek -> return 6
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

            parsedList.add(str.substring(classStart+2,prepStart-4))
            parsedList.add(str.substring(prepStart+3,grStart-2))
            parsedList.add(str.substring(grStart+2,str.length-1))


        }else{


           parsedList.add(str.substring(classStart+2,grStart-2))
           parsedList.add("null")
           parsedList.add(str.substring(grStart+2,str.length-1))


        }
        return parsedList
    }
}
