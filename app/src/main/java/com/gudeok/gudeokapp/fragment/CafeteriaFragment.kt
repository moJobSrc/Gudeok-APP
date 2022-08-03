package com.gudeok.gudeokapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gudeok.gudeokapp.R
import com.gudeok.gudeokapp.adapter.CalendarAdapter
import com.gudeok.gudeokapp.data.DayModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class CafeteriaFragment : Fragment() {


    private val defaultDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var defaultMonthFormat: DateFormat = SimpleDateFormat("yyyy-MM")
    var selectedDate = ""


    private var startDate = Date()
    private var selectedDateTime: Date? = null
    private var beforeIB: ImageButton? = null
    private var afterIB: ImageButton? = null
    private var dateTV: TextView? = null


    var calendarRecyclerView: RecyclerView? = null
    private var calendarAdapter: CalendarAdapter? = null

    private val nowDate = Date() //주에 오늘 날짜가 포함하는지 보기위해.

    private val nowCal = Calendar.getInstance()
    private val nowMonth = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cafeteria, container, false)

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        calendarAdapter = CalendarAdapter(requireContext())
        calendarRecyclerView!!.adapter = calendarAdapter
        beforeIB = view.findViewById<ImageButton>(R.id.beforeIB)
        afterIB = view.findViewById<ImageButton>(R.id.afterIB)
        dateTV = view.findViewById<TextView>(R.id.dateTV)

        beforeIB!!.setOnClickListener { v: View? ->
            setDate(
                -1
            )
        }
        afterIB!!.setOnClickListener { v: View? ->
            setDate(
                1
            )
        }

        selectedDateTime = Calendar.getInstance().time

        setCalendarList(
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH]
        )
        return view
    }

    private fun setDate(addAmount: Int) {
        val calendar = Calendar.getInstance()
        if (selectedDateTime != null) {
            calendar.time = selectedDateTime
        }
        calendar.add(Calendar.MONTH, addAmount)
        startDate = calendar.time
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH]] = 1
        val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH]] = max
        selectedDate = defaultDateFormat.format(nowDate)
        selectedDateTime = calendar.time //strStartDate;
        val setMonth = calendar[Calendar.MONTH] + 1
        if (setMonth == nowMonth) {
            selectedDate = defaultDateFormat.format(nowDate)
            selectedDateTime = calendar.time //strStartDate;
        } else {
            val dashboardDateCal = Calendar.getInstance()
            dashboardDateCal[calendar[Calendar.YEAR], calendar[Calendar.MONTH]] = 1
            selectedDate = defaultDateFormat.format(dashboardDateCal.time) //strStartDate;
            selectedDateTime = calendar.time //strStartDate;
        }
        if (selectedDateTime == null) {
            calendar.time = startDate
            selectedDateTime = startDate
        } else {
            calendar.time = selectedDateTime
        }
        setCalendarList(calendar[Calendar.YEAR], calendar[Calendar.MONTH])
    }

    fun setCalendarList(year: Int, month: Int) {
        try {
            dateTV!!.text = defaultMonthFormat.format(selectedDateTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val dayModelList: MutableList<DayModel> = ArrayList<DayModel>()
        try {
            val calendar = GregorianCalendar()
            val preCalendar = GregorianCalendar()
            if (year > 0 && month > -1) {
                calendar[GregorianCalendar.YEAR] = year
                calendar[GregorianCalendar.MONTH] = month
                calendar[GregorianCalendar.DATE] = 1
                preCalendar[GregorianCalendar.YEAR] = year
                preCalendar[GregorianCalendar.MONTH] = month
                preCalendar[GregorianCalendar.DATE] = 1
            } else {
                calendar[GregorianCalendar.YEAR] = calendar[Calendar.YEAR]
                calendar[GregorianCalendar.MONTH] = calendar[Calendar.MONTH]
                calendar[GregorianCalendar.DATE] = 1
                preCalendar[GregorianCalendar.YEAR] = calendar[Calendar.YEAR]
                preCalendar[GregorianCalendar.MONTH] = calendar[Calendar.MONTH]
                preCalendar[GregorianCalendar.DATE] = 1
            }
            preCalendar.add(Calendar.MONTH, -1)
            val dayOfWeek =
                calendar[Calendar.DAY_OF_WEEK] - 1 //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
            val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) // 해당 월에 마지막 요일
            val preMonthMax = preCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            //앞의 EMPTY 생성
            for (j in 0 until dayOfWeek) {
                val dayModel = DayModel()
                dayModel.setType(102031)
                dayModel.setCalendarModel(
                    GregorianCalendar(
                        calendar[Calendar.YEAR],
                        calendar[Calendar.MONTH] - 1, preMonthMax - (dayOfWeek - 1 - j)
                    )
                )
                dayModelList.add(dayModel)
            }
            if (dayOfWeek > 0) {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val d: Date = Date(dayModelList[0].getCalendarModel().getTimeInMillis())
            }
            for (j in 1..max) {
                val dayModel = DayModel()
                dayModel.setType(1)
                dayModel.setCalendarModel(
                    GregorianCalendar(
                        calendar[Calendar.YEAR],
                        calendar[Calendar.MONTH], j
                    )
                )
                dayModelList.add(dayModel)
            }

            //뒤의 Empty
            if (dayModelList.size / 7 > 0) {
                var extra = dayModelList.size % 7
                if (extra > 0) {
                    extra = 7 - extra
                }
                for (k in 0 until extra) {
                    val dayModel = DayModel()
                    dayModel.setType(102031)
                    dayModel.setCalendarModel(
                        GregorianCalendar(
                            calendar[Calendar.YEAR],
                            calendar[Calendar.MONTH] + 1,
                            k + 1
                        )
                    )
                    dayModelList.add(dayModel)
                }
                if (extra > 0) {
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val d: Date = Date(
                        dayModelList[dayModelList.size - 1].getCalendarModel().getTimeInMillis()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        calendarAdapter!!.mCalendarList = dayModelList
        calendarAdapter!!.notifyDataSetChanged()
    }
}