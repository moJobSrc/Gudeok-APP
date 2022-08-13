package com.gudeok.gudeokapp.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Util {
    private val calendar: Calendar = Calendar.getInstance()
    private val dateFmt: DateFormat = SimpleDateFormat("yyyyMMdd")

    //return Monday Date
    fun getWeekStart(): String {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return dateFmt.format(calendar.time)
    }

    //return Friday Date
    fun getWeekEnd(): String {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
        return dateFmt.format(calendar.time)
    }

    //return Today Date
    fun getTodayDate(): String {
        val date = Date(System.currentTimeMillis())
        return dateFmt.format(date)
    }
}