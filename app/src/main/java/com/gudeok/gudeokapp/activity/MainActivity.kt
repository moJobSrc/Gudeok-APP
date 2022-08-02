package com.gudeok.gudeokapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gudeok.gudeokapp.R
import com.gudeok.gudeokapp.databinding.ActivityMainBinding
import com.gudeok.gudeokapp.fragment.*

class MainActivity : AppCompatActivity() {

    //https://open.neis.go.kr/hub/mealServiceDietInfo?Type=json&pIndex=1&pSize=1&ATPT_OFCDC_SC_CODE=C10&SD_SCHUL_CODE=7150087&MLSV_FROM_YMD=20220327&MLSV_TO_YMD=20220327

    lateinit var binding: ActivityMainBinding
    lateinit var toolbar_title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingToolbar()
        binding.apply {
            if (intent.hasExtra("msg")) {
//                Snackbar.make(mainTitle, "${intent.getStringExtra("msg")}", Snackbar.LENGTH_LONG)
//                    .show()
            }
            setListenerBottomNavigation()
            supportFragmentManager.beginTransaction().add(R.id.fragment, HomeFragment()).commit()
        }
    }

    @SuppressLint("UseSupportActionBar")
    private fun bindingToolbar() {
        binding.apply {
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            toolbar_title = findViewById<TextView>(R.id.toolbar_title)
            toolbar_title.text = "구덕고등학교"
//            setActionBar(toolbar)
//            supportActionBar?.apply {
//                toolbar.title = ""
//                toolbar.subtitle = ""
//                setDisplayShowCustomEnabled(true);
//                setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
//                setDisplayHomeAsUpEnabled(true);
//            }
        }
    }

    private fun setListenerBottomNavigation() {
        binding.bottomNavigation.menu.getItem(2).isChecked = true
        binding.bottomNavigation.setOnItemSelectedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
            when(it.itemId) {
                R.id.menu_main -> {
                    if (fragment is HomeFragment)
                        return@setOnItemSelectedListener false
                    toolbar_title.text = "구덕고등학교"
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.menu_setting -> {
                    if (fragment is SettingFragment)
                        return@setOnItemSelectedListener false
                    toolbar_title.text = "설정"
                    replaceFragment(SettingFragment())
                    true
                }
                R.id.menu_commnunity -> {
                    if (fragment is CommunityFragment)
                        return@setOnItemSelectedListener false
                    toolbar_title.text = "커뮤니티"
                    replaceFragment(CommunityFragment())
                    true
                }
                R.id.menu_schedule -> {
                    if (fragment is ScheduleFragment)
                        return@setOnItemSelectedListener false
                    toolbar_title.text = "시간표"
                    replaceFragment(ScheduleFragment())
                    true
                }
                R.id.menu_restaurant -> {
                    if (fragment is CafeteriaFragment)
                        return@setOnItemSelectedListener false
                    toolbar_title.text = "급식"
                    replaceFragment(CafeteriaFragment())
                    true
                }
                else -> false
            }
        }
    }
    private fun replaceFragment(fragmentLayout: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragmentLayout)
            .commitAllowingStateLoss()
    }
}