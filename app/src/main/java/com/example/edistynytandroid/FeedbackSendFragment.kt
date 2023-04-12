package com.example.edistynytandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.edistynytandroid.databinding.FragmentFeedbackReadBinding
import com.example.edistynytandroid.databinding.FragmentFeedbackSendBinding


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

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
