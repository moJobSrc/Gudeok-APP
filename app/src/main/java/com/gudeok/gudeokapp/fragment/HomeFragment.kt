package com.gudeok.gudeokapp.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import com.gudeok.gudeokapp.BuildConfig
import com.gudeok.gudeokapp.R
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build()
    lateinit var tempText:TextView
    lateinit var skyText: TextView
    lateinit var lunchText: TextView
    lateinit var dinnerText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        tempText = root.findViewById(R.id.tempText)
        skyText = root.findViewById(R.id.skyText)
        lunchText = root.findViewById(R.id.lunchText)
        dinnerText = root.findViewById(R.id.dinnerText)

        val date = Date(System.currentTimeMillis())

        val getDay = SimpleDateFormat("yyyyMMdd").format(date)
        val getTime = SimpleDateFormat("HH00").format(date)
        Log.i("datetime.../",getDay + getTime)
        lookUpWeather(getDay,getTime)
        getCafeteria(Date(), LunchType.Lunch)
        getCafeteria(Date(), LunchType.Dinner)

        return root
    }

    enum class LunchType(val type: String) {
        Lunch("2"),
        Dinner("3")
    }

    private fun getCafeteria(date: Date, type: LunchType) {
        Log.d("급식", type.toString())
        val request = Request.Builder()
            .url("https://open.neis.go.kr/hub/mealServiceDietInfo?" +
                    "Type=json&pIndex=1&pSize=1&ATPT_OFCDC_SC_CODE=C10&SD_SCHUL_CODE=7150087" +
                    "&MMEAL_SC_CODE=${type.type}&MLSV_FROM_YMD=20220806&MLSV_TO_YMD=20220806" +
                    "&key=${BuildConfig.NEIS_KEY}")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    var content = response.body!!.string()
                    //                Log.d("급식", content)
                    var mealServiceDietInfo =
                        JSONObject(content).getJSONArray("mealServiceDietInfo").getJSONObject(1)
                    //                Log.d("급식", mealServiceDietInfo.toString())
                    var row = mealServiceDietInfo.getJSONArray("row").getJSONObject(0)
                    //                Log.d("급식", row.toString())
                    var DDISH_NM = row.getString("DDISH_NM").replace("<br/>", "\n").trim()
                        .replace("\\([^)]*\\)".toRegex(), " ").replace("\\s+".toRegex(), "\n")
                    Log.d("급식", DDISH_NM)

                    activity?.runOnUiThread {
                        if (type.toString().equals("Lunch")) {
                            lunchText.setText(DDISH_NM)
                        } else {
                            dinnerText.setText(DDISH_NM)
                        }
                    }
                } catch (e: java.lang.Exception){
                    e.printStackTrace()
                    activity?.runOnUiThread {
                        if (type.toString().equals("Lunch")) {
                            lunchText.setText("급식 정보를 가져오지\n못하였습니다.")
                        } else {
                            dinnerText.setText("급식 정보를 가져오지\n못하였습니다.")
                        }
                    }
                }
            }

        })

    }

    data class WeatherData(
        @SerializedName("baseData") val baseDate: String,
        @SerializedName("baseTime") val baseTime: String,
        @SerializedName("category") val category: String,
        @SerializedName("fcstDate") val fcstDate: String,
        @SerializedName("fcstTime") val fcstTime: String,
        @SerializedName("fcstValue") val fcstValue: String,
        @SerializedName("nx") val nx: String,
        @SerializedName("ny") val ny: String,
    )

    @Throws(IOException::class, JSONException::class)
    fun lookUpWeather(baseDate: String, time: String) {
        var weather = ""
        var temperature = ""
        val contentMessage by lazy {
            getString(R.string.weatherTemp, temperature)
        }
        val baseTime = timeChange(time) //"0500" 조회하고 싶은 시간
        // 로딩 텍스트
        tempText.text = "로딩중입니다.."
        // 홈페이지에서 받은 키
        val serviceKey =
            "e%2BE34vxmWfjVQxtz3yvzeTnghYXewlok7FK5jEX7ffIBm9L%2Fky35XT7CYYWxkfBgfPS22GXw9UzQREBRIRVnpQ%3D%3D"
        // "e%25252BE34vxmWfjVQxtz3yvzeTnghYXewlok7FK5jEX7ffIBm9L%25252Fky35XT7CYYWxkfBgfPS22GXw9UzQREBRIRVnpQ%25253D%25253D"

        val httpUrl: HttpUrl = HttpUrl.Builder()
            .scheme("http")
            .host("apis.data.go.kr")
            .addPathSegment("1360000")
            .addPathSegment("VilageFcstInfoService_2.0")
            .addPathSegment("getVilageFcst")
            .addEncodedQueryParameter("ServiceKey", serviceKey)
            .addQueryParameter("pageNo", "1")
            .addQueryParameter("nx", "96")
            .addQueryParameter("ny", "75")
            .addQueryParameter("base_date", baseDate)
            .addQueryParameter("base_time", baseTime)
            .addQueryParameter("dataType", "json")
            .build()
        Log.d("url", httpUrl.toString())
        val builder = Request.Builder()
            .url(httpUrl)
            .get()
            .build()
        client.newCall(builder).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body!!.string()
                Log.d("response", res)
                //정각이 넘어가면 오류 발생  timeChange 함수 변경 필요
                try {
                    val json = JsonParser.parseString(res).asJsonObject
                    val list = Gson().fromJson(json["response"].asJsonObject["body"].asJsonObject["items"].asJsonObject["item"].asJsonArray.toString(), Array<WeatherData>::class.java)
                    for (item in list) {
                        if (item.category == "SKY") {
                            when(item.fcstValue){
                                "1" -> weather = "맑음"
                                "2" -> weather = "비"
                                "3" -> weather = "구름"
                                "4" -> weather = "흐림"
                            }
                        }
                        if (item.category == "TMP") {
                            temperature = item.fcstValue
                        }
                    }
                    activity?.runOnUiThread {
                        if (context != null) {
                            tempText.text = contentMessage
                            skyText.text = weather
                        }
                    }
                } catch (e: Exception) {
                    activity?.runOnUiThread {
                        tempText.text = "날씨 정보를 가져올수없습니다."
                        skyText.text = weather
                    }
                }

            }
        })
    }


    private fun timeChange(time: String): String {
        // 현재 시간에 따라 데이터 시간 설정(3시간 마다 업데이트) //
        /**
         * 시간은 3시간 단위로 조회해야 한다. 안그러면 정보가 없다고 뜬다.
         * 0200, 0500, 0800 ~ 2300까지
         * 그래서 시간을 입력했을때 switch문으로 조회 가능한 시간대로 변경해주었다.
         */
        var time = time
        time = when (time) {
            "0200", "0300", "0400" -> "0200"
            "0500", "0600", "0700" -> "0500"
            "0800", "0900", "1000" -> "0800"
            "1100", "1200", "1300" -> "1100"
            "1400", "1500", "1600" -> "1400"
            "1700", "1800", "1900" -> "1700"
            "2000", "2100", "2200" -> "2000"
            else -> "2300"
        }
        return time
    }

}