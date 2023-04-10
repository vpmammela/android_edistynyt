package com.example.edistynytandroid

import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.edistynytandroid.databinding.FragmentToDoDataBinding
import com.example.edistynytandroid.datatypes.post.Post
import com.example.edistynytandroid.datatypes.todo.Todo
import com.google.gson.GsonBuilder
import java.io.UnsupportedEncodingException


class ToDoDataFragment : Fragment() {
    private var _binding: FragmentToDoDataBinding? = null

    // This property is only valid between onCreateView and // onDestroyView.
    private val binding get() = _binding!!

    // alustetaan viittaus adapteriin sekä luodaan LinearLayoutManager
// RecyclerView tarvitsee jonkin LayoutManagerin, joista yksinkertaisin on Linear
    private lateinit var adapter: ToDoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToDoDataBinding.inflate(inflater, container, false)

        val root: View = binding.root

        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerViewToDo.layoutManager = linearLayoutManager

        binding.buttonGetTodos.setOnClickListener {
            getTodos()
        }

        binding.buttonPostTodo.setOnClickListener {
            postTodos()
        }
        binding.buttonGetPosts.setOnClickListener {
            getPosts()
        }
        return root
    }

    fun getTodos() {
        val JSON_URL = "https://jsonplaceholder.typicode.com/todos"

        val gson = GsonBuilder().setPrettyPrinting().create()

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("TESTI", response)
                var rows : List<Todo> = gson.fromJson(response, Array<Todo>::class.java).toList()

                adapter = ToDoAdapter(rows)
                binding.recyclerViewToDo.adapter = adapter

                for (item: Todo in rows) {
                    Log.d("TESTI", "Title: " + item.title)
                }

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

    fun postTodos() {
        val JSON_URL = "https://jsonplaceholder.typicode.com/todos"

        var testPost : Todo = Todo()
        testPost.userId = 1
        testPost.id = 3456
        testPost.title = "testi todo"
        testPost.completed = false

        var gson = GsonBuilder().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, JSON_URL,
            Response.Listener { response ->
                // usually APIs return the added new data back
                // when sending new data
                // therefore the response here should contain the JSON version
                // of the data you just sent below
                Log.d("TESTI", response)
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("TESTI", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // we have to specify a proper header, otherwise the API might block our queries!
                // define that we are after JSON data!
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
                    var newData = gson.toJson(testPost)

                    // create a new TodoItem object here, and convert it to string format (GSON)

                    // JSON to bytes
                    body = newData.toByteArray(Charsets.UTF_8)
                } catch (e: UnsupportedEncodingException) {
                    // problems with converting our data into UTF-8 bytes
                }
                return body
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        // if using this in an activity, use "this" instead of "context"
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    fun getPosts() {
        val JSON_URL = "https://jsonplaceholder.typicode.com/posts"

        val gson = GsonBuilder().setPrettyPrinting().create()

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("TESTI", response)
                var rows : List<Post> = gson.fromJson(response, Array<Post>::class.java).toList()

                for (item: Post in rows) {
                    Log.d("TESTI", "Title: " + item.body)
                }

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