package com.example.scheduler

import com.example.scheduler.ScheduleProcessing.Para

public interface ShowBottomFragmentDialogSearch{
    public fun groupChanged(newGroup:String)
}
public interface ShowBottomFragmentDialogParaInfo{
    public fun showParaInfo(para:Para)
    public fun editParaInfo(para:Para)

}

public interface EditSchedule{

    public fun tryToSave(dayOfWeek: Int, weekType: Int, numb: Int, isOutside:Boolean,para: Para, classRoom:String):Pair<Boolean, String>
}
