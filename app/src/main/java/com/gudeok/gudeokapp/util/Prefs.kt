package com.gudeok.gudeokapp.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefNm="gudeok"
    private val prefs=context.getSharedPreferences(prefNm, MODE_PRIVATE)

    var token:String?
        get() = prefs.getString("token",null)
        set(value){
            prefs.edit().putString("token",value).apply()
        }

    fun getInstance(): SharedPreferences {
        return prefs
    }


}