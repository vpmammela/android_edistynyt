package com.example.edistynytandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.edistynytandroid.databinding.FragmentDataBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataFragment : Fragment() {
    private var _binding: FragmentDataBinding? = null

    // This property is only valid between onCreateView and // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataBinding.inflate(inflater, container, false)

        val root: View = binding.root
// navigate to another fragment, pass some parameter too
        binding.buttonNavigateDetail.setOnClickListener {

            val action = DataFragmentDirections.actionDataFragmentToDetailFragment(3245)
            it.findNavController().navigate(action)
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
