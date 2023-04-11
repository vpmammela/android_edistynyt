package com.example.edistynytandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.edistynytandroid.databinding.FragmentOpenStreetMapsBinding
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.config.Configuration.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class OpenStreetMapsFragment : Fragment() {
    private var _binding: FragmentOpenStreetMapsBinding? = null

    // This property is only valid between onCreateView and // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenStreetMapsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(activity as Context))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.


        binding.map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = binding.map.controller
        mapController.setZoom(19.0)
        val startPoint = GeoPoint(66.50247438013193, 25.7300978471244);
        mapController.setCenter(startPoint);

        val firstMarker = Marker(binding.map)
        firstMarker.position = startPoint
        firstMarker.title = "Rovaniemi"
        firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        binding.map.overlays.add(firstMarker)
        binding.map.invalidate()

        firstMarker.setOnMarkerClickListener { marker, mapView ->
            Log.d("TESTI", "Marker click")
            return@setOnMarkerClickListener false
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}