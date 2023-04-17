package com.example.edistynytandroid

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytandroid.databinding.FragmentFeedbackReadBinding
import com.example.edistynytandroid.datatypes.feedback.Feedback
import com.google.gson.GsonBuilder
import org.json.JSONObject


class FeedbackReadFragment : Fragment() {
    private var _binding: FragmentFeedbackReadBinding? = null

    // This property is only valid between onCreateView and // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackReadBinding.inflate(inflater, container, false)

        val root: View = binding.root
// navigate to another fragment, pass some parameter too

        getFeedbacks()

        return root
    }

    fun getFeedbacks() {

        var feedbacks : List<Feedback> = emptyList()
        val gson = GsonBuilder().setPrettyPrinting().create()
        // this is the url where we want to get our data
        // Note: if using a local server, use http://10.0.2.2 for localhost. this is a virtual address for Android emulators, since
        // localhost refers to the Android device instead of your computer
        val JSON_URL = "http://10.0.2.2:8055/items/feedback?access_token=L_LruBFG3SiZWRFwKTA17XLyC9WnFQAL"

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                Log.d("TESTI", response)

                val jObject = JSONObject(response)
                val jArray = jObject.getJSONArray("data")

                feedbacks = gson.fromJson(jArray.toString() , Array<Feedback>::class.java).toList()

                for (item: Feedback in feedbacks) {
                    Log.d("TESTI", item.location.toString())
                }

                // luodaan adapteri ListViewille, voit korvata tämän RecyclerViewillä myös!
                // Huom: tämä ei toimi suoraan, johtuen ListViewin vaatimuksesta
                // käyttää vain Stringejä, ks. punainen kommentti alempaa
                val adapter = ArrayAdapter(activity as Context, R.layout.simple_list_item_1, feedbacks)

                // muista myös lisätä ListView fragmentin ulkoasuun (xml)
                // ListView löytyy Design-valikosta otsikon "Legacy" alta
                binding.listViewFeedbacks.adapter = adapter
                binding.listViewFeedbacks.setAdapter(adapter)

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