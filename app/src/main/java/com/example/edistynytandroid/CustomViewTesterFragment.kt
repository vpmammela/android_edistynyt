package com.example.edistynytandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.edistynytandroid.databinding.FragmentCustomViewTesterBinding
import com.example.edistynytandroid.databinding.FragmentDataBinding


class CustomViewTesterFragment : Fragment() {
    private var _binding: FragmentCustomViewTesterBinding? = null

    // This property is only valid between onCreateView and // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomViewTesterBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.speedView.speedTo(35f)

        binding.customTemperatureViewTest.changeTemperature(14)

        binding.buttonChangeTemperature.setOnClickListener {
            val randomTemperature : Int = (-40..40).random()
            binding.customTemperatureViewTest.changeTemperature(randomTemperature)
            Log.d("TESTI", randomTemperature.toString())
        }

        binding.buttonAddDataTest.setOnClickListener {
            val randomValue : Int = (1..100).random()
            binding.latestDataViewTest.addData("Testing " + randomValue.toString())
        }


        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
