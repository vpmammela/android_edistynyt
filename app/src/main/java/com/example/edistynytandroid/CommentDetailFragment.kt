package com.example.edistynytandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.edistynytandroid.databinding.FragmentCommentDetailBinding


class CommentDetailFragment : Fragment() {
    private var _binding: FragmentCommentDetailBinding? = null

    // get fragment parameters from previous fragment
    val args: CommentDetailFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // print out the given parameter into logs
        Log.d("TESTI", "Kommentin id-parametri: " + args.id.toString())

        // tästä eteenpäin detail fragmentin voi tehdä pääpiirteittäin kolmella eri tavalla
        // detail fragmentin idea tässä tapauksessa on ottaa vastaan tai hakea yhden kommentin datat
        // ja asettaa ne esim. erillisiin TextVieweihin (tee itse TextViewit tämän fragmentin ulkoasuun)
        // ja kytke ne haluamallasi tavalla

        // vaihtoehto 1: lähetetään Detail fragmentiin pelkkä kommentin id
        // idea on se että rakennetaan uusi URL/osoite rajapintaan, ja haetaan vain yksi kommentti

        // vaihtoehto 2: lähetetään detail fragmentiin kaikki kommentin muuttujat omina argumentteinaan
        // ja tulostetaan ne sellaisenaan. hyvä puoli: helppo ratkaisu, huono puoli: jos data kerkeää muuttua
        // välissä rajapinnassa data-palvelussa, silloin mobiilisovellus näyttää vanhentunutta dataa

        // vaihtoehto 3: lähetetään detail fragmentiin JSON-muodossa oleva Comment-objekti, ja puretaan
        // se tässä fragmentissa GSONin avulla Comment-objektiksi

        // jos mennään vaihtoehdolla 1, saadaan aina ajantasaisin data rajapinnasta
        // tätä varten tarvitaan generoitu yhden kommentin URL/osoite, joka voidaan hakea, esim:

        // this is the url where we want to get our data from
        val JSON_URL = "https://jsonplaceholder.typicode.com/comments/" + args.id
        Log.d("TESTI", JSON_URL)

        // tätä JSON_URLia hyödyntämällä, lataa Volley-koodilla raakadata, ja muuta se
        // GSONilla objektimuotoon:
        // var item : TodoItem = gson.fromJson(response, TodoItem::class.java)

        // tämän jälkeen item-muuttujan avulla, asetetaan arvot minne halutaan:
        // binding.jokutextview.text = item.email jne.

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}