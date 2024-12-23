package com.submission.storyapps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.submission.storyapps.databinding.ActivityMapsBinding
import com.submission.storyapps.model.Story
import com.submission.storyapps.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fetchStoriesAndDisplayMarkers()
    }

    private fun fetchStoriesAndDisplayMarkers() {
        val apiService = ApiClient.create(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getAllStoriesWithLocation(1)
                withContext(Dispatchers.Main) {
                    if (!response.error) {
                        addMarkersToMap(response.listStory)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun addMarkersToMap(stories: List<Story>) {
        stories.filter { it.lat != null && it.lon != null }.forEach { story ->
            val position = LatLng(story.lat!!, story.lon!!)
            val markerOptions = MarkerOptions()
                .position(position)
                .title(story.name)
                .snippet(story.description)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            mMap.addMarker(markerOptions)
        }

        if (stories.isNotEmpty() && stories[0].lat != null && stories[0].lon != null) {
            val firstLocation = LatLng(stories[0].lat!!, stories[0].lon!!)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MapsActivity::class.java)
            context.startActivity(intent)
        }
    }
}
