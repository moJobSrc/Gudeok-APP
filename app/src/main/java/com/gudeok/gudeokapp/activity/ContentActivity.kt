package com.gudeok.gudeokapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gudeok.gudeokapp.databinding.ActivityContentBinding
import kotlin.properties.Delegates

class ContentActivity : AppCompatActivity() {

    lateinit var binding: ActivityContentBinding
    var post_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("post_id")) {
            post_id = intent.getIntExtra("post_id", -1)
        }

    }
}