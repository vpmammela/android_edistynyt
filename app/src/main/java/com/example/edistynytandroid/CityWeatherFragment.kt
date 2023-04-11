package com.example.edistynytandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytandroid.databinding.FragmentCityWeatherBinding
import com.example.edistynytandroid.datatypes.cityweather.CityWeather
import com.google.gson.GsonBuilder


class CityWeatherFragment : Fragment() {
    private var _binding: FragmentCityWeatherBinding? = null

    // get fragment parameters from previous fragment
    val args: CityWeatherFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityWeatherBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d("TESTI", "lat: " + args.lat.toString())
        Log.d("TESTI", "lon: " + args.lon.toString())

        getWeatherData()

        return root
    }

    fun getWeatherData() {
        // this is the url where we want to get our data from

        // haetaan local.properties -tiedostosta muuttuja
        val API_KEY = BuildConfig.OPENWEATHER_API_KEY
        // OpenWeatherMap-osoite on tätä muotoa:
        // https://api.openweathermap.org/data/2.5/weather?lat=66.50247&lon=25.73009&units=metric&appid=OMAKEYTAHAN
        val JSON_URL = "https://api.openweathermap.org/data/2.5/weather?lat=${args.lat}&lon=${args.lon}&units=metric&appid=${API_KEY}"
        val gson = GsonBuilder().setPrettyPrinting().create()
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->


                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("TESTI", response)
                var item : CityWeather = gson.fromJson(response, CityWeather::class.java)

                val temperature = item.main?.temp
                val humidity = item.main?.humidity

                Log.d("TESTI", "lämpötila: ${temperature}C, kosteusprosentti: ${humidity}%")

            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("TESTI", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {

                // basic headers for the data
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        // if using this in an activity, use "this" instead of "context"
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}