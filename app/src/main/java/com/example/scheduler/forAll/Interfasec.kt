package com.example.scheduler.forAll

import com.example.scheduler.scheduleProcessing.Para

interface ShowBottomFragmentDialogSearch {
    fun groupChanged(newGroup: String)
    fun codeChanged(newCode: String)

}
interface ChangeTabByCode{
    fun changeGroup(newGroup: String){

    }
}

interface GetPostSchedule {
    suspend fun postSchedule(url: String, pageNumb: Int)
    fun getGroup(pageNumb: Int): String?
    fun makeBadge()

}

interface ShowBottomFragmentDialogParaInfo {
    fun showParaInfo(para: Para)
    fun editParaInfo(para: Para)

}

interface InvaludateSchedule {
    fun invalidateSchedule()
}

interface EditSchedule {

    fun tryToSave(
        dayOfWeek: Int,
        weekType: Int,
        numb: Int,
        isOutside: Boolean,
        para: Para,
        classRoom: String
    ): Pair<Boolean, String>
}
