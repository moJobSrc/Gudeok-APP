package com.gudeok.gudeokapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gudeok.gudeokapp.R
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    lateinit var tempText:TextView
    lateinit var skyText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_home, container, false)

        tempText = root.findViewById(R.id.tempText)
        skyText = root.findViewById(R.id.skyText)

        NetworkThread2().start()

        return root
    }

    // 네트워크를 이용할 때는 쓰레드를 사용해서 접근해야 함
    inner class NetworkThread2 : Thread() {
         override fun run() {
//            val wd = WeatherData()
            var weather = ""
            try {
                // date에 값을 넣어야함, 오늘 날짜 기준으로 넣기!
                // ex) date = "20210722", time = "0500"
                val date = Date(System.currentTimeMillis())

                val getDay = SimpleDateFormat("yyyyMMdd").format(date)
                val getTime = SimpleDateFormat("HH00").format(date)
                Log.i("datetime.../",getDay + getTime)

                weather = lookUpWeather(getDay, getTime)
            } catch (e: IOException) {
                Log.i("THREE_ERROR1", e.message!!)
            } catch (e: JSONException) {
                Log.i("THREE_ERROR2", e.message!!)
            }
            Log.i("현재날씨", weather!!)
            var asdklfjalksdjasdf = weather.split("/")
            tempText.setText(asdklfjalksdjasdf[0])
            skyText.setText(asdklfjalksdjasdf[1])

         }
    }

    @Throws(IOException::class, JSONException::class)
    fun lookUpWeather(baseDate: String?, time: String?): String {
        var weather = ""
        var tmperature = ""

        val baseTime = timeChange(time) //"0500" 조회하고 싶은 시간
        val type = "json" //타입
        val apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"

//         홈페이지에서 받은 키
        val serviceKey =
            "e%2BE34vxmWfjVQxtz3yvzeTnghYXewlok7FK5jEX7ffIBm9L%2Fky35XT7CYYWxkfBgfPS22GXw9UzQREBRIRVnpQ%3D%3D"
        val urlBuilder = StringBuilder(apiUrl)
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8").toString() + "=" + serviceKey)
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8").toString() + "=" + URLEncoder.encode("96", "UTF-8")) //경도
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8").toString() + "=" + URLEncoder.encode("75", "UTF-8")) //위도
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8").toString() + "=" + URLEncoder.encode(baseDate, "UTF-8")) /* 조회하고 싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8").toString() + "=" + URLEncoder.encode(baseTime, "UTF-8")) /* 조회하고 싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8").toString() + "=" + URLEncoder.encode(type, "UTF-8")) /* 타입 */

        // GET방식으로 전송해서 파라미터 받아오기
        val url = URL(urlBuilder.toString())
//        System.out.println(url);  //어떻게 넘어가는지 확인하고 싶으면 출력분 주석 해제

        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.setRequestMethod("GET")
        conn.setRequestProperty("Content-type", "application/json")
        val rd: BufferedReader
        rd = if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            BufferedReader(InputStreamReader(conn.getInputStream()))
        } else {
            BufferedReader(InputStreamReader(conn.getErrorStream()))
        }
        val sb = StringBuilder()
        var line: String?
        while (rd.readLine().also { line = it } != null) {
            sb.append(line)
        }
        rd.close()
        conn.disconnect()
        val result = sb.toString()

        // response 키를 가지고 데이터를 파싱
        val jsonObj_1 = JSONObject(result)
        val response = jsonObj_1.getString("response")

        // response 로 부터 body 찾기
        val jsonObj_2 = JSONObject(response)
        val body = jsonObj_2.getString("body")

        // body 로 부터 items 찾기
        val jsonObj_3 = JSONObject(body)
        val items = jsonObj_3.getString("items")
        Log.i("ITEMS", items)

        // items로 부터 itemlist 를 받기
        var jsonObj_4 = JSONObject(items)
        val jsonArray = jsonObj_4.getJSONArray("item")
        for (i in 0 until jsonArray.length()) {
            jsonObj_4 = jsonArray.getJSONObject(i)
            val fcstValue = jsonObj_4.getString("fcstValue")
            val category = jsonObj_4.getString("category")
            if (category == "SKY") {
                when(fcstValue){
                    "1" -> weather = "맑음"
                    "2" -> weather = "비"
                    "3" -> weather = "구름"
                    "4" -> weather = "흐림"
                }
            }
            if (category == "TMP") {
                tmperature = "$fcstValue℃"
            }
        }
        return "${tmperature}/${weather}"
    }


    fun timeChange(time: String?): String? {
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