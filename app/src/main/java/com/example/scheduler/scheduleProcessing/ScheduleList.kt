package com.example.scheduler.scheduleProcessing

import android.util.Log
import com.example.scheduler.forAll.EditSchedule

class ScheduleList : EditSchedule {

    var days = arrayListOf(
        Day(0, this),
        Day(1, this),
        Day(2, this),
        Day(3, this),
        Day(4, this),
        Day(5, this),
        Day(6, this),
        Day(7, this),
    )

    fun savePara(
        dayOfWeek: Int,
        weekType: Int,
        numb: Int,
        isOutside: Boolean,
        para: Para,
        classRoom: String
    ) {
        if
                (para.isOutside) days[7].paras.remove(para)
        else
            days[para.dayOfWeek].paras.remove(para)




        para.dayOfWeek = dayOfWeek
        para.weekType = weekType
        para.numb = numb
        para.classRoom = classRoom



        if (isOutside) {
            para.isOutside = true
            days[7].paras.add(para)
            days[7].paras.sortBy { it.sub }
            Log.d("ew", days[7].paras.size.toString())

        } else {
            para.isOutside = false

            days[dayOfWeek].addPara(para)
            Log.d("ew", days[dayOfWeek].paras.size.toString())
            days[dayOfWeek].paras.sortBy { it.numb }

        }


    }


    override fun tryToSave(
        dayOfWeek: Int,
        weekType: Int,
        numb: Int,
        isOutside: Boolean,
        para: Para,
        classRoom: String
    ): Pair<Boolean, String> {
        var res: Pair<Boolean, String>

        Log.d("ew", "попытка переместить в $dayOfWeek, $weekType, $numb ")
        if (para.dayOfWeek == 7) {
            if (isOutside) {
                res = Pair(true, "пара ${para.sub} изменена вне сетки")


            } else {
                res = isPossibleToSave(dayOfWeek, weekType, numb, para)


            }
        } else {
            if (isOutside) {
                res = Pair(true, "пара ${para.sub} теперь вне сетки")

            } else {
                res = isPossibleToSave(dayOfWeek, weekType, numb, para)


            }
        }

        if (res.first)
            savePara(dayOfWeek, weekType, numb, isOutside, para, classRoom)

        return res
    }

    private fun isPossibleToSave(
        dayOfWeek: Int,
        weekType: Int,
        numb: Int,
        para: Para,
    ): Pair<Boolean, String> {
        var parasAtNumb = ArrayList<Int>()

        for (i in 0..days[dayOfWeek].paras.size - 1) {
            if (days[dayOfWeek].paras[i].numb == numb) parasAtNumb.add(i)
        }
        when (parasAtNumb.size) {
            0 -> return Pair(true, "это место свободно, можно добавить ${para.sub}")
            1 -> {
                if (para.numb == numb && para.dayOfWeek == dayOfWeek) return Pair(
                    true,
                    "параметры пары: ${para.sub} изменены без перемещения"
                )
                else {
                    if (days[dayOfWeek].paras[parasAtNumb[0]].weekType == 0) return Pair(
                        false,
                        "это место каждую неделю занимает пара ${days[dayOfWeek].paras[parasAtNumb[0]].sub}"
                    )
                    else
                        if (days[dayOfWeek].paras[parasAtNumb[0]].weekType != weekType && weekType != 0) return Pair(
                            true,
                            "это место в одну из неделю занимает пара ${days[dayOfWeek].paras[parasAtNumb[0]].sub} а в другую теперь ${para.sub}"
                        )
                        else {
                            if (days[dayOfWeek].paras[parasAtNumb[0]].weekType == 1 && (weekType == 1 || weekType == 0)) return Pair(
                                false,
                                "это место в синюю неделю занимает пара ${days[dayOfWeek].paras[parasAtNumb[0]].sub}"
                            )
                            else
                                if (days[dayOfWeek].paras[parasAtNumb[0]].weekType == 2 && (weekType == 2 || weekType == 0)) return Pair(
                                    false,
                                    "это место в красную неделю занимает пара ${days[dayOfWeek].paras[parasAtNumb[0]].sub}"
                                )

                        }

                }
            }

            2 -> {
                if (para.numb == numb && para.dayOfWeek == dayOfWeek && para.weekType == weekType) return Pair(
                    true,
                    "аудитория пары: ${para.sub} изменена"
                )
                else return Pair(
                    false,
                    "эта пара занята ${days[dayOfWeek].paras[parasAtNumb[0]].sub} по синей и ${days[dayOfWeek].paras[parasAtNumb[1]].sub} по красной"
                )
            }
        }

        return Pair(false, "необработанная попытка сохранить")

    }


}

class Day(var dayOfWeek: Int, var schedule: EditSchedule) {
    var paras = ArrayList<Para>(0)


    fun addPara(para: Para) {

        para.dayOfWeek = dayOfWeek
        para.schedule = schedule
        if (dayOfWeek == 7) para.isOutside = true

        paras.add(para)

    }

    fun addPara(para: Para, dayOfWeek: Int) {
        addPara(para)
        paras.last().dayOfWeek = dayOfWeek

    }


}

class Para(

    public val sub: String,
    public val prepod: String,
    public var classRoom: String,
    public val typeOfSubject: String,
    public val groups: String,
    public var weekType: Int = 0,
    public var numb: Int
) {

    public var dayOfWeek: Int = 0
    public var isOutside = false

    public lateinit var schedule: EditSchedule

    public fun getStartTime(): String {
        return Times.StartTimes.stTime[numb - 1]
    }

    public fun getEndTime(): String {
        return Times.EndTimes.stTime[numb - 1]
    }

    fun getNumbStr(): String {

        return numb.toString()
    }

    fun SaveChanges(
        dayOfWeek: Int,
        weekType: Int,
        numb: Int,
        isOutside: Boolean,
        classRoom: String
    ): Pair<Boolean, String> {
        return schedule.tryToSave(dayOfWeek, weekType, numb, isOutside, this, classRoom)


    }


}