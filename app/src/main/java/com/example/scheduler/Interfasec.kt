package com.example.scheduler

import com.example.scheduler.ScheduleProcessing.Para

public interface ShowBottomFragmentDialogSearch{
    public fun groupChanged(newGroup:String)
}
public interface ShowBottomFragmentDialogParaInfo{
    public fun showParaInfo(para:Para)

}
public interface GroupSaving{
    public fun saveGroup(group:String)
}
