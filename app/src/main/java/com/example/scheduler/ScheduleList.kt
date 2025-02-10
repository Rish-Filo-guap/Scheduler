package com.example.scheduler

class ScheduleList {
 val weeks= arrayOf(
     Week( arrayOf(
             Day(arrayOf(
                 Para(Subject.OVP, Prepods.Drozd, Classroom.L1307, TypeOfSubject.Pra ,5),
                 Para(Subject.OVP, Prepods.Bahtev, Classroom.L1319,TypeOfSubject.Lek, 6)
             )),
             Day(arrayOf(
                 Para(Subject.BigData, Prepods.Bogoslov, Classroom.B3203, TypeOfSubject.Lek ,2),
                 Para(Subject.Ether, Prepods.Shevakov, Classroom.B3305,TypeOfSubject.Lab, 3),
                 Para(Subject.BigData, Prepods.Bogoslov, Classroom.B3305, TypeOfSubject.Lab ,4),
             )),
             Day(arrayOf(
                 Para(Subject.Methods, Prepods.Brezjev, Classroom.B3304, TypeOfSubject.Lab ,2),

                 Para(Subject.Model, Prepods.Tatar, Classroom.B5219, TypeOfSubject.Lek ,4),
             )),
             Day(arrayOf(
                 Para(Subject.Ether, Prepods.Shevakov, Classroom.B5304, TypeOfSubject.Lek ,2),
                 Para(Subject.Ether, Prepods.Shevakov, Classroom.B3302, TypeOfSubject.Lab ,3),
                 Para(Subject.Web, Prepods.Krasiln, Classroom.B5218, TypeOfSubject.Lek ,4),
                 Para(Subject.Web, Prepods.Krasiln, Classroom.B2317, TypeOfSubject.Lab ,5),
             )),
             Day(arrayOf(
                 Para(Subject.Methods, Prepods.Brezjev, Classroom.B5304, TypeOfSubject.Lek ,1),
                 Para(Subject.Phizra, Prepods.Perel, Classroom.S, TypeOfSubject.Pra ,2),

             )),
            Day(arrayOf()),
            Day(arrayOf()),
     )),


     Week( arrayOf(
         Day(arrayOf(
             Para(Subject.OVP, Prepods.Drozd, Classroom.L1307, TypeOfSubject.Pra ,5),
             Para(Subject.OVP, Prepods.Bahtev, Classroom.L1319,TypeOfSubject.Lek, 6)
         )),
         Day(arrayOf(
            // Para(Subject.BigData, Prepods.Bogoslov, Classroom.B3203, TypeOfSubject.Lek ,2),
             Para(Subject.Design, Prepods.Syet, Classroom.B1314,TypeOfSubject.Lek, 3),
             Para(Subject.BigData, Prepods.Bogoslov, Classroom.B3305, TypeOfSubject.Lab ,4),
         )),
         Day(arrayOf(
             Para(Subject.Methods, Prepods.Brezjev, Classroom.B3304, TypeOfSubject.Lab ,2),
             Para(Subject.Design, Prepods.Syet, Classroom.B3307,TypeOfSubject.Lab, 3),
             Para(Subject.Model, Prepods.Tatar, Classroom.B5219, TypeOfSubject.Lek ,4),
         )),
         Day(arrayOf(
             //Para(Subject.Ether, Prepods.Shevakov, Classroom.B5304, TypeOfSubject.Lek ,2),
             //Para(Subject.Ether, Prepods.Shevakov, Classroom.B3302, TypeOfSubject.Lab ,3),
             Para(Subject.Web, Prepods.Krasiln, Classroom.B5218, TypeOfSubject.Lek ,4),
             Para(Subject.Web, Prepods.Krasiln, Classroom.B2317, TypeOfSubject.Lab ,5),
             Para(Subject.Model, Prepods.Savel, Classroom.B2317, TypeOfSubject.Lab ,6),
         )),
         Day(arrayOf(
             Para(Subject.Methods, Prepods.Brezjev, Classroom.B5304, TypeOfSubject.Lek ,1),
             Para(Subject.Phizra, Prepods.Perel, Classroom.S, TypeOfSubject.Pra ,2),

             )),
         Day(arrayOf()),
         Day(arrayOf()),
     )),







 )
}
class Week(val days:Array<Day>){

}
class Day(val paras:Array<Para>){

}

class Para (
    public val sub:Subject,
    public val prepod:Prepods,
    public val classRoom:Classroom,
    public val typeOfSubject: TypeOfSubject,
    public val numb:Int) {

    public fun GetStartTime():String{
        return Times.StartTimes.stTime[numb-1]
    }
    public fun GetEndTime():String{
        return Times.EndTimes.stTime[numb-1]
    }

}