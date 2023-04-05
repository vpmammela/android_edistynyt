package com.example.edistynytandroid

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.edistynytandroid.databinding.RecyclerViewItemBinding
import com.example.edistynytandroid.datatypes.comment.Comment

// aloitetaan luomalla uusi luokka CommentHolder
class CommentAdapter(private val comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    // tähän väliin tulee kaikki RecyclerView-adapterin vaatimat metodit
    // kuten onCreateViewHolder, onBindViewHolder sekä getItemCount

    // binding layerin muuttujien alustaminen
    private var _binding: RecyclerViewItemBinding? = null
    private val binding get() = _binding!!

    // ViewHolderin onCreate-metodi. käytännössä tässä kytketään binding layer
    // osaksi CommentHolder-luokkaan (adapterin sisäinen luokka)
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        // binding layerina toimii yksitätinen recyclerview_item_row.xml -instanssi
        _binding =
            RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    // tämä metodi kytkee yksittäisen Comment-objektin yksittäisen CommentHolder-instanssiin
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä onBindViewHolder
    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val itemComment = comments[position]
        holder.bindComment(itemComment)
    }

    // Adapterin täytyy pysty tietämään sisältämänsä datan koko tämän metodin avulla
    // koska CommentAdapter pohjautuu RecyclerViewin perusadapteriin, täytyy tästä
    // luokasta löytyä metodi nimeltä getItemCount
    override fun getItemCount(): Int {
        return comments.size

    }

    class CommentHolder(v: RecyclerViewItemBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        // tämän kommentin ulkoasu ja varsinainen data
        private var view: RecyclerViewItemBinding = v
        private var comment: Comment? = null

        // mahdollistetaan yksittäisen itemin klikkaaminen tässä luokassa
        init {
            //tämä mahdollistaa sen että kun kommenttia klikataan
            // suoritetaan alla oleva onClick
            v.root.setOnClickListener(this)
        }

        // metodi, joka kytkee datan yksityiskohdat ulkoasun yksityiskohtiin
        fun bindComment(comment : Comment)
        {
            this.comment = comment

            // tallenetaan kommentin nimi muuttujaan
            var text : String = comment.name.toString()

            if(text.length > 20) {
                text = text.substring(0, 20) + "..."
            }

            // jos pituus on yli 20 merkkiä, lyhennetään
            view.textViewCommentName.text = text
            view.textViewCommentEmail.text = comment.email
            view.textViewCommentBody.text = comment.body
        }

        // jos itemiä klikataan käyttöliittymässä, ajetaan tämä koodio
        override fun onClick(v: View) {
            Log.d("TESTI", "recyclerView-Comment klikattu")
            Log.d("TESTI", "Comment ID = " + comment?.id.toString())

            val action = CommentDataFragmentDirections.actionCommentDataFragmentToCommentDetailFragment(comment?.id as Int)
            v.findNavController().navigate(action)
        }
    }
}