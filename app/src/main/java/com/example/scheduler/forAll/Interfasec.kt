package com.example.scheduler.forAll

import com.example.scheduler.scheduleProcessing.Para

public interface ShowBottomFragmentDialogSearch{
    public fun groupChanged(newGroup:String)

}
public interface GetPostSchedule{
    public suspend fun  postSchedule(url:String, pageNumb:Int)
    public fun getGroup(pageNumb: Int):String?

}
public interface ShowBottomFragmentDialogParaInfo{
    public fun showParaInfo(para:Para)
    public fun editParaInfo(para:Para)

}
public interface InvaludateSchedule{
    public fun invalidateSchedule()
}

public interface EditSchedule{

    public fun tryToSave(dayOfWeek: Int, weekType: Int, numb: Int, isOutside:Boolean,para: Para, classRoom:String):Pair<Boolean, String>
}
