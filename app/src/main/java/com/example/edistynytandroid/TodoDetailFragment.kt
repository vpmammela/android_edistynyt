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
import com.example.edistynytandroid.databinding.FragmentCommentDetailBinding
import com.example.edistynytandroid.databinding.FragmentTodoDetailBinding
import com.example.edistynytandroid.datatypes.todo.Todo
import com.example.edistynytandroid.datatypes.user.User
import com.google.gson.GsonBuilder


class TodoDetailFragment : Fragment() {
    private var _binding: FragmentTodoDetailBinding? = null

    // get fragment parameters from previous fragment
    val args: TodoDetailFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textViewTodoId.text = args.id.toString()
        binding.textViewTodoTitle.text = args.title
        binding.textViewTodoCompleted.text = args.completed.toString()

        getUser()

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getUser() {


        val JSON_URL = "https://jsonplaceholder.typicode.com/users/" + args.userId.toString()

        val gson = GsonBuilder().setPrettyPrinting().create()

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                var item : User = gson.fromJson(response, User::class.java)

                var userName = item.name
                binding.textViewTodoUserName?.text = userName.toString()


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
}