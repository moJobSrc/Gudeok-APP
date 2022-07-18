package com.gudeok.gudeokapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView
import com.gudeok.gudeokapp.R


class ScheduleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        val timetable = root.findViewById(R.id.timetable) as TimetableView
        val schedules = arrayListOf<Schedule>()
        val schedule = Schedule()
        schedule.startTime = Time(10, 0)
        schedule.startTime = Time(13, 30)
        schedule.classTitle = "기가"
        schedule.day = 1
        schedules.add(schedule)
        timetable.add(schedules)
        return root
    }

    private fun getScheduleTime(perio: String): String {
        /**
         * 시간은 3시간 단위로 조회해야 한다. 안그러면 정보가 없다고 뜬다.
         * 0200, 0500, 0800 ~ 2300까지
         * 그래서 시간을 입력했을때 switch문으로 조회 가능한 시간대로 변경해주었다.
         */
        var perio = perio
        perio = when (perio) {
            "1" -> "0200"
            "2" -> "0500"
            "3" -> "0800"
            "4" -> "1100"
            "5" -> "1400"
            "6" -> "1700"
            "7" -> "2000"
            else -> "2300"
        }
        return perio
    }
}