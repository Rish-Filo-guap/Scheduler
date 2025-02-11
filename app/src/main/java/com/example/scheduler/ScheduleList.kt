package com.example.scheduler

class ScheduleList {
var weeks= arrayListOf(
     Week(
        arrayListOf(
            Day(),
            Day(),
            Day(),
            Day(),
            Day(),
            Day(),
            Day(),
        )
     ),
     Week(
         arrayListOf(
             Day(),
             Day(),
             Day(),
             Day(),
             Day(),
             Day(),
             Day(),
         )
     ),
     )

}
class Week(val days:ArrayList<Day>){

}
class Day(){
    var paras=ArrayList<Para>(0)
    constructor(paras:ArrayList<Para>) : this() {
        this.paras=paras
    }
    fun addPara(para: Para){
        paras.add(para)
    }
}

class Para (
    public val sub:String,
    public val prepod:String,
    public val classRoom:String,
    public val typeOfSubject: String,
    public val groups: String,
    public val numb:Int) {

    public fun GetStartTime():String{
        return Times.StartTimes.stTime[numb-1]
    }
    public fun GetEndTime():String{
        return Times.EndTimes.stTime[numb-1]
    }

}