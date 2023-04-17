package com.example.edistynytandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytandroid.databinding.FragmentFeedbackReadBinding
import com.example.edistynytandroid.databinding.FragmentFeedbackSendBinding
import com.example.edistynytandroid.datatypes.feedback.Feedback
import com.google.gson.GsonBuilder
import java.io.UnsupportedEncodingException


class FeedbackSendFragment : Fragment() {
    private var _binding: FragmentFeedbackSendBinding? = null

    // This property is only valid between onCreateView and // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackSendBinding.inflate(inflater, container, false)

        val root: View = binding.root
// navigate to another fragment, pass some parameter too

        binding.buttonSendFeedback.setOnClickListener {
            val name = binding.editTextFeedbackName.text.toString()
            val location = binding.editTextFeedbackLocation.text.toString()
            val value = binding.editTextFeedbackValue.text.toString()

            Log.d("TESTI", "${name}-${location}-${value}")

            sendFeedback(name, location, value)
        }
        return root
    }

    fun sendFeedback(name: String, location: String, value: String) {
        // this is the url where we want to get our data
        // Note: if using a local server, use http://10.0.2.2 for localhost. this is a virtual address for Android emulators, since
        // localhost refers to the Android device instead of your computer
        val JSON_URL = "http://10.0.2.2:8055/items/feedback?access_token=L_LruBFG3SiZWRFwKTA17XLyC9WnFQAL"

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, JSON_URL,
            Response.Listener { response ->

                Log.d("TESTI", "Uusi feedback tallennettu")

                binding.editTextFeedbackName.setText("")
                binding.editTextFeedbackLocation.setText("")
                binding.editTextFeedbackValue.setText("")

                Toast.makeText(context, "Thank you for your feedback", Toast.LENGTH_LONG).show()

                val action = FeedbackSendFragmentDirections.actionFeedbackSendFragmentToFeedbackReadFragment()
                findNavController().navigate(action)

                // response from API, you can use this in TextView, for example
                // Check also out the example below
                // "Handling the JSON in the Volley response" for this part

                // Note: if you send data to API instead, this might not be needed
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("TESTI", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // we have to specify a proper header, otherwise Apigility will block our queries!
                // define we are after JSON data!
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }

            // let's build the new data here
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                // this function is only needed when sending data
                var body = ByteArray(0)
                try { // check the example "Converting a Kotlin object to JSON"
                    // on how to create this newData -variable
                    // create a new TodoItem object here, and convert it to string format (GSON)

                    var f : Feedback = Feedback();
                    f.location = location
                    f.name = name
                    f.value = value

                    // muutetaan Feedback-olio -> JSONiksi
                    var gson = GsonBuilder().create();
                    var newData = gson.toJson(f);

                    // JSON to bytes
                    body = newData.toByteArray(Charsets.UTF_8)
                } catch (e: UnsupportedEncodingException) {
                    // problems with converting our data into UTF-8 bytes
                }
                return body
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
