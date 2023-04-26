package com.example.edistynytandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.edistynytandroid.databinding.FragmentRemoteMessageBinding
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import kotlin.random.Random


class RemoteMessageFragment : Fragment() {
    private var _binding: FragmentRemoteMessageBinding? = null
    private lateinit var client: Mqtt3AsyncClient

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRemoteMessageBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.buttonSendRemoteMessage.setOnClickListener {
            var randomNumber = Random.nextInt(0, 100)
            var stringPayload = "Hello world " + randomNumber.toString()

            client.publishWith()
                .topic(BuildConfig.HIVEMQ_TOPIC)
                .payload(stringPayload.toByteArray())
                .send()
        }
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier("AndroidHiveMQTTtest")
            .serverHost(BuildConfig.HIVEMQ_BROKER)
            .serverPort(8883)
            .buildAsync()

        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.HIVEMQ_USERNAME)
            .password(BuildConfig.HIVEMQ_PASSWORD.toByteArray())
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
            .topicFilter(BuildConfig.HIVEMQ_TOPIC)
            .callback { publish ->

                // this callback runs everytime your code receives new data payload
                var result = String(publish.getPayloadAsBytes())

                try {
                    activity?.runOnUiThread {
                        binding.textViewRemoteMessage.text = result
                    }
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