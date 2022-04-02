package com.gudeok.gudeokapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gudeok.gudeokapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //https://open.neis.go.kr/hub/mealServiceDietInfo?Type=json&pIndex=1&pSize=1&ATPT_OFCDC_SC_CODE=C10&SD_SCHUL_CODE=7150087&MLSV_FROM_YMD=20220327&MLSV_TO_YMD=20220327

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding


    }
}