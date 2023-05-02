package com.example.edistynytandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.edistynytandroid.databinding.FragmentWeatherStationBinding
import com.example.edistynytandroid.datatypes.weatherstation.WeatherStation
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.util.*
import android.widget.ProgressBar


class WeatherStationFragment : Fragment() {
    private var _binding: FragmentWeatherStationBinding? = null
    private lateinit var client: Mqtt3AsyncClient
    private lateinit var progressBar: ProgressBar
    private var progressStatus = 0
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherStationBinding.inflate(inflater, container, false)

        val root: View = binding.root
        progressBar = binding.progressBar
        progressBar.progress = 0
        progressStatus = 0
        progressBar.max = 50
        progressBar.min = -50

        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier(BuildConfig.MQTT_CLIENT_ID + UUID.randomUUID().toString())
            .serverHost(BuildConfig.MQTT_BROKER)
            .serverPort(8883)
            .buildAsync()

        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.MQTT_USERNAME)
            .password(BuildConfig.MQTT_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    Log.d("TESTI", "Connection failure.")
                } else {
                    // Setup subscribes or start publishing
                    subscribeToTopic()
                }
            }

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        client.disconnect()
    }

    fun subscribeToTopic() {
        val gson = GsonBuilder().setPrettyPrinting().create()

        client.subscribeWith()
            .topicFilter(BuildConfig.MQTT_TOPIC)
            .callback { publish ->

                    // this callback runs everytime your code receives new data payload
                    var result = String(publish.getPayloadAsBytes())

                    try {
                    var item : WeatherStation = gson.fromJson(result, WeatherStation::class.java)
                    Log.d("TESTI", item.d.get1().v.toString() + "C")

                    val temperature = item.d.get1().v
                    var humidity = item.d.get3().v
                    var text = "Temperature: ${temperature}°C"
                        text += "\n"
                        text += "Humidity: ${humidity}%"

                    activity?.runOnUiThread{
                        binding.textViewWeatherText.text = text
                    }

                        binding.progressBar.progress = temperature.toInt()

                }
                catch (e: java.lang.Exception) {
                    Log.d("TESTI", e.message.toString())
                    Log.d("TESTI", "Saattaa olla diagnostiikkadataa")
                }
            }
            .send()
            .whenComplete { subAck, throwable ->
                if (throwable != null) {
                    // Handle failure to subscribe
                    Log.d("TESTI", "Subscribe failed.")
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    Log.d("TESTI", "Subscribed!")
                }
            }
    }
}