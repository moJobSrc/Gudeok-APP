package com.gudeok.gudeokapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gudeok.gudeokapp.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity() {

    lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}