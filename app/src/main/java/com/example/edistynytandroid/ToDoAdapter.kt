package com.example.edistynytandroid

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.green
import androidx.recyclerview.widget.RecyclerView
import com.example.edistynytandroid.databinding.RecyclerviewTodoRowBinding
import com.example.edistynytandroid.datatypes.todo.Todo

class ToDoAdapter(private val todos: List<Todo>) : RecyclerView.Adapter<ToDoAdapter.ToDoHolder>() {

    private var _binding: RecyclerviewTodoRowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
        _binding = RecyclerviewTodoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoHolder, position: Int) {
        val itemTodo = todos[position]
        holder.bindTodo(itemTodo)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    class ToDoHolder(v: RecyclerviewTodoRowBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {
        private var view: RecyclerviewTodoRowBinding = v
        private var todo: Todo? = null

        init {

        }

        fun bindTodo(todo: Todo) {
            this.todo = todo

            view.textViewTodoTitle.text = todo.title
            if (todo.completed == true) {
                view.textViewCompletedIcon.text = "\u2714"
                view.textViewCompletedIcon.setTextColor(Color.GREEN)
            } else {
                view.textViewCompletedIcon.text = "\u2716"
                view.textViewCompletedIcon.setTextColor(Color.RED)
            }

        }

        override fun onClick(v: View?) {

        }
    }
}