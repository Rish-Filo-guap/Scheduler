package com.example.scheduler.ScheduleProcessing

class ScheduleList {
    var days= arrayListOf(
        Day(0),
        Day(1),
        Day(2),
        Day(3),
        Day(4),
        Day(5),
        Day(6),
        Day(7),
    )


}
class Day(var dayOfWeek: Int){
    var paras=ArrayList<Para>(0)
    var isEmpty=true
    init{
        paras.add(Para("","","Выходной", "", "",0 ,8) )
    }
    fun addPara(para: Para){
        if(isEmpty)
            paras.clear()
        isEmpty=false
        para.dayOfWeek=dayOfWeek
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
    public var dayOfWeek: Int=0

    public fun getStartTime():String{
        return Times.StartTimes.stTime[numb-1]
    }
    public fun getEndTime():String{
        return Times.EndTimes.stTime[numb-1]
    }
    fun getNumbStr():String{
        if(numb==8) return ""
        else return numb.toString()
    }
}