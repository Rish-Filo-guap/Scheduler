package com.example.scheduler.ScheduleProcessing

class ScheduleList {
    var days= arrayListOf(
        Day(),
        Day(),
        Day(),
        Day(),
        Day(),
        Day(),
        Day(),
    )


}
class Day{
    var paras=ArrayList<Para>(0)
    var isEmpty=true
    constructor(){
        paras.add(Para("","","Выходной", "", "",0 ,7) )
    }
    fun addPara(para: Para){
        if(isEmpty)
            paras.clear()
        isEmpty=false
        paras.add(para)
    }
}

class Para (
    public val sub:String,
    public val prepod:String,
    public val classRoom:String,
    public val typeOfSubject: String,
    public val groups: String,
    public val weekType:Int=0,
    public val numb:Int) {

    public fun getStartTime():String{
        return Times.StartTimes.stTime[numb-1]
    }
    public fun getEndTime():String{
        return Times.EndTimes.stTime[numb-1]
    }
    fun getNumbStr():String{
        if(numb==7) return ""
        else return numb.toString()
    }
}