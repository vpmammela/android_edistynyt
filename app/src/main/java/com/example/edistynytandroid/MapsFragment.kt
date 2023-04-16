package com.example.edistynytandroid

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.edistynytandroid.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // tehdään luokan ylätasolle uusi jäsenmuuttuja => gMao
    private lateinit var gMap : GoogleMap


    private val callback = OnMapReadyCallback { googleMap ->

        // laitetaan googleMap olio talteen että sitä voi käyttää onCreateViewissä
        gMap = googleMap

        // tehdään sydney marker
        val sydney = LatLng(-33.86833079928604, 151.21346536937173)
        var m1 = googleMap.addMarker(MarkerOptions().position(sydney).title("Sydney"))
        m1?.tag = "Sydney"

        val kempele = LatLng(64.91294583780142, 25.503441270130505)
        var m2 = googleMap.addMarker(MarkerOptions().position(kempele).title("Kempele"))
        m2?.tag = "Kempele"

        // siirretään mapsin kamera tähän koordinaattiin
        // muutetaan zoomia, float
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kempele, 15f))

        googleMap.setOnMarkerClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // käytetään binding layeria myös karttafragmentissa

        binding.checkBoxZoomControls.setOnCheckedChangeListener { compoundButton, b ->
            gMap.uiSettings.isZoomControlsEnabled = b
        }

        binding.radioButtonMapNormal.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        binding.radioButtonMapHybrid.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }

        binding.radioButtonMapTerrain.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("TESTI", "Marker CLICK!")

        // klikatun markerin koordinaatit löytyvät p0-muuttujan positionista:
        Log.d("TESTI", p0.position.latitude.toString())
        Log.d("TESTI", p0.position.longitude.toString())

        // jos markerilla on tagi, tällä tavalla sen saa haettua siitä
        Log.d("TESTI", p0.tag.toString())

        // tallennetaan koordinaatit apumuuttujiin selkeyden vuoksi
        val lat = p0.position.latitude.toFloat()
        val lon = p0.position.longitude.toFloat()

        // actionin avulla siirrytään CityWeatherFragmentiin ja lähetetään
        // tarvittavat koordinaatit parametreina
        val action = MapsFragmentDirections.actionMapsFragmentToCityWeatherFragment(lat, lon)
        findNavController().navigate(action)

        return false
    }
}