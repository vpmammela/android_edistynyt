package com.example.edistynytandroid

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytandroid.databinding.FragmentFeedbackDetailBinding
import com.example.edistynytandroid.datatypes.feedback.Feedback
import com.google.gson.GsonBuilder
import java.io.UnsupportedEncodingException


class FeedbackDetailFragment : Fragment() {
    private var _binding: FragmentFeedbackDetailBinding? = null
    // This property is only valid between onCreateView and // onDestroyView.
    val args: FeedbackDetailFragmentArgs by navArgs()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackDetailBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val editableName = Editable.Factory.getInstance().newEditable(args.name)
        binding.editTextEditFeedbackName.text = editableName

        val editableLocation = Editable.Factory.getInstance().newEditable(args.location)
        binding.editTextEditFeedbackLocation.text = editableLocation

        val editableValue = Editable.Factory.getInstance().newEditable(args.value)
        binding.editTextEditFeedbackValue.text = editableValue

        binding.buttonUpdateFeedback.setOnClickListener {
            val name = binding.editTextEditFeedbackName.text.toString()
            val location = binding.editTextEditFeedbackLocation.text.toString()
            val value = binding.editTextEditFeedbackValue.text.toString()

            updateFeedback(args.id.toString(), name, location, value)
        }

        return root }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateFeedback(id: String, name: String, location: String, value: String) {
        val JSON_URL = "http://10.0.2.2:8055/items/feedback/${id}?access_token=L_LruBFG3SiZWRFwKTA17XLyC9WnFQAL"

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.PATCH, JSON_URL,
            Response.Listener { response ->

                Log.d("TESTI", "feedback päivitetty")


                Toast.makeText(context, "Feedback is now updated", Toast.LENGTH_LONG).show()

                val action = FeedbackDetailFragmentDirections.actionFeedbackDetailFragmentToFeedbackReadFragment()
                findNavController().navigate(action)
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
    }
