package com.example.edistynytandroid.ui.cityWeather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytandroid.BuildConfig
import com.google.gson.GsonBuilder

class CityWeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<CityWeather>()
    val weatherData: LiveData<CityWeather> get() = _weatherData

    fun getWeatherData(lat: Float, lon: Float) {
        val API_KEY = BuildConfig.OPENWEATHER_API_KEY
        val JSON_URL =
            "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$API_KEY"

        val gson = GsonBuilder().setPrettyPrinting().create()

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->
                val item: CityWeather = gson.fromJson(response, CityWeather::class.java)
                _weatherData.value = item
            },
            Response.ErrorListener {
                Log.d("TESTI", it.toString())
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(ApplicationProvider.getApplicationContext())
        requestQueue.add(stringRequest)
    }
}
