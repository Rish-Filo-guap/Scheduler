package com.example.scheduler



enum class Prepods(val prepod: String){
    Krasiln("Красильникова Ольга Ивановна"),
    Bogoslov("Богословская Наталья Валентиновна"),
    Shevakov("Шевяков Даниил Олегович"),
    Brezjev("Бржезовский Александр Викторович"),
    Tatar("Татарникова Татьяна Михайловна"),
    Bahtev("Бахтеев Алексей Павлович"),
    Syet("Суетина Татьяна Александровна"),
    Perel("Перельман Михаил Борисович"),
    Savel("Савельева Дарья Дмитриевна"),
    Drozd("Дроздецкий Сергей Игоревич")
}

enum class TypeOfSubject(val typeSub:String){
    Lab("Лабораторное занятие"),
    Lek("Лекция"),
    Pra("Практическое занятие"),
    Kyr("Курсовая работа"),
    No("Null")
}
enum class Times(val stTime:Array<String>){
    StartTimes(arrayOf("9:30", "11:10", "13:00", "15:00", "16:40", "18:30", "с утра")),
    EndTimes(arrayOf("11:00", "12:40", "14:30", "16:30", "18:10", "20:00", "до вечера"));




}
enum class DaysOfWeek(val dayOfWeek: String){
    Mon("Понедельник"),
    Tue("Вторник"),
    Wed("Среда"),
    Thu("Четверг"),
    Fri("Пятница"),
    Sat("Суббота"),
    Sun("Воскресенье")
}
