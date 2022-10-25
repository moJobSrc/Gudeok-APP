package com.gudeok.gudeokapp.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView
import com.google.gson.JsonParser
import com.gudeok.gudeokapp.BuildConfig
import com.gudeok.gudeokapp.R
import com.gudeok.gudeokapp.util.Dlog
import com.gudeok.gudeokapp.util.Util
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ScheduleFragment : Fragment() {

    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }

    val colors = arrayOf("#f08676", "#ecc369", "#a7ca70", "#7dd1c1", "#7aa5e9", "#fbaa68", "#9f86e1", "#78cb87", "#d397ed", "#f64f8c", "#6f88a2", "#8b5182")

    private val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build()
    val schedules = ArrayList<Schedule>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        getSchedule(root)
        return root
    }

    private fun getSchedule(root: View) {
        val timetable = root.findViewById<TimetableView>(R.id.timetable)
        /**
        ATPT_OFCDC_SC_CODE	     STRING(필수)	    시도교육청코드
        SD_SCHUL_CODE	         STRING(필수)	    표준학교코드
        AY	                     STRING(선택)	    학년도
        SEM	                     STRING(선택)	    학기
        ALL_TI_YMD	             STRING(선택)	    시간표일자
        GRADE	                 STRING(선택)	    학년
        CLASS_NM	             STRING(선택)	    반명
        TI_FROM_YMD	             STRING(선택)	    시간표시작일자
        TI_TO_YMD	             STRING(선택)	    시간표종료일자
        KEY                      STRING(필수)       개인키
         */
        val request = Request.Builder()
            .url("https://open.neis.go.kr/hub/hisTimetable?key=${BuildConfig.NEIS_KEY}&Type=json&pIndex=1" +
                    "&pSize=100&ATPT_OFCDC_SC_CODE=C10&SD_SCHUL_CODE=7150087" +
                    "&GRADE=1&CLASS_NM=6&TI_FROM_YMD=${Util.getWeekStart()}&TI_TO_YMD=${Util.getWeekEnd()}")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val response = response.body!!

                try {
                    val jsonParser = JsonParser.parseString(response.string())
                    val jsonObject =
                        jsonParser.asJsonObject["hisTimetable"].asJsonArray[1].asJsonObject
                    val jsonArray = jsonObject["row"].asJsonArray
                    val colorMap = HashMap<String, String>()
                    var index = 0;

                    jsonArray.forEachIndexed { _, jsonElement ->
                        val schedule = Schedule()
                        val sc_class = jsonElement.asJsonObject
                        val perio = getScheduleTime(sc_class["PERIO"].asString)
                        val title = sc_class["ITRT_CNTNT"].asString
                        val date = sc_class["ALL_TI_YMD"].asString //20201231
                        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                        val localdate = LocalDate.parse(date, formatter).atStartOfDay();

                        schedule.classTitle = title
                        schedule.day = localdate.dayOfWeek.value - 1 //월 ~ 일을 1 ~ 7로 리턴해주기때문에 1을 빼준다
                        schedule.startTime = perio[0]
                        schedule.endTime = perio[1]
                        if (!colorMap.containsKey(title)) {
                            index++
                            if (index > colors.size - 1) {
                                index -= (colors.size - 1);
                                colorMap[title] = colors[index]
                            } else {
                                colorMap[title] = colors[index]
                            }
                            schedule.colorCode = colorMap[title]
                        } else {
                            schedule.colorCode = colorMap[title]
                        }

                        schedules.add(schedule)
                    }
                    //UI 수정을 메인쓰레드 아닌곳에서 할시 오류
                    activity?.runOnUiThread {
                        timetable.add(schedules)
                    }

                } catch (e: IllegalStateException) { // 학교 시간표 업데이트가 되지않아 반환하는 값 없음 ex) {"RESULT":{"CODE":"INFO-200","MESSAGE":"해당하는 데이터가 없습니다."}}
                    Dlog.e(e.message)
                } catch (e: Exception) {
                    Dlog.e(e.message)
                }
            }

        })
    }

    private fun getScheduleTime(perio: String): Array<Time> {
        /**
         * 학교 각 교시 시간을 변환해서 보내준다
         * 1교시 -> 08:40 ~ 09:30
         * 2교시 -> 09:40 ~ 10:30
         * 3교시 -> 10:40 ~ 11:30
         * 4교시 -> 11:40 ~ 12:30
         * 5교시 -> 13:30 ~ 14:20
         * 6교시 -> 14:40 ~ 15:30
         * 7교시 -> 15:40 ~ 16:30
         */
        val time: Array<Time> = when (perio) {
            "1" -> arrayOf(Time(1, 0), Time(2, 0))
            "2" -> arrayOf(Time(2, 0), Time(3, 0))
            "3" -> arrayOf(Time(3,0), Time(4, 0))
            "4" -> arrayOf(Time(4, 0), Time(5, 0))
            "5" -> arrayOf(Time(5, 0), Time(6, 0))
            "6" -> arrayOf(Time(6, 0), Time(7, 0))
            "7" -> arrayOf(Time(7, 0), Time(8, 0))
            else -> arrayOf(Time(0, 0), Time(0, 0))
        }
        return time
    }
}