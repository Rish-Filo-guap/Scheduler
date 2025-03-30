package com.example.scheduler

import com.example.scheduler.scheduleProcessing.GetInfoFromEther
import org.junit.Test

import org.junit.Assert.*
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun testUrl(){
        val res= URL("https://guap.ru/rasp/?gr=5950").readText()
        println(res)
    }
}