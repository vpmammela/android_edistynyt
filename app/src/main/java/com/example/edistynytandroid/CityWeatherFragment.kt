package com.example.edistynytandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.edistynytandroid.databinding.FragmentCityWeatherBinding



class CityWeatherFragment : Fragment() {
    private var _binding: FragmentCityWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CityWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityWeatherBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(CityWeatherViewModel::class.java)

        val args: CityWeatherFragmentArgs by navArgs()
        Log.d("TESTI", "lat: " + args.lat.toString())
        Log.d("TESTI", "lon: " + args.lon.toString())

        viewModel.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
            weatherData?.let {
                binding.textViewWeatherCityName.text = it.name.toString()
                binding.textViewWeatherTemperature.text = it.main?.temp.toString() + " C"
                binding.textViewWeatherHumidity.text = it.main?.humidity.toString() + " %"
                binding.textViewWeatherWindSpeed.text = it.wind?.speed.toString() + " m/s"
            }
        })

        viewModel.getWeatherData(args.lat, args.lon)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
