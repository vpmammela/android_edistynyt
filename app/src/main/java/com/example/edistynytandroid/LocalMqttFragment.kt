package com.example.edistynytandroid

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.edistynytandroid.databinding.FragmentLocalMqttBinding
import com.example.edistynytandroid.databinding.FragmentRemoteMessageBinding
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck


class LocalMqttFragment : Fragment() {
    private var _binding: FragmentLocalMqttBinding? = null
    private lateinit var client: Mqtt3AsyncClient

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocalMqttBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("NOT WORKING")
        builder.setMessage("I couldn't get this to connect to mosquitto running on mac! \nReturn to home fragment")
        builder.setPositiveButton("Yes") { _, _ ->
            val action = LocalMqttFragmentDirections.actionLocalMqttFragmentToNavHome()
            findNavController().navigate(action)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()

        binding.buttonSendRemoteMessage.setOnClickListener {
            var stringPayload = binding.editTextSendMessage.text.toString()
            binding.editTextSendMessage.text.clear()
            client.publishWith()
                .topic(BuildConfig.LOCAL_MQTT_TOPIC)
                .payload(stringPayload.toByteArray())
                .send()
        }

        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier("testaaja")
            .serverHost(BuildConfig.LOCAL_MQTT_BROKER)
            .serverPort(1883)
            .buildAsync()

        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.LOCAL_MQTT_USERNAME)
            .password(BuildConfig.LOCAL_MQTT_PASSWORD.toByteArray())
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
            .topicFilter(BuildConfig.LOCAL_MQTT_TOPIC)
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