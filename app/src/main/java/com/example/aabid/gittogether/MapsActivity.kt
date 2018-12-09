package com.example.aabid.gittogether

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.widget.Toast
import com.example.aabid.gittogether.data.User

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private  lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        database = FirebaseDatabase.getInstance().reference

        //initRecyclerView()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val marker = LatLng(47.0, 19.0)
        mMap.addMarker(MarkerOptions().position(marker).title("Marker"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))

        mMap.isTrafficEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.setOnMapClickListener {
            val markerOpt = MarkerOptions()
                .position(it)
                .title("My Marker ${it.latitude}, ${it.longitude}")

            val markerNew = mMap.addMarker(markerOpt)

            markerNew.isDraggable = true

            mMap.animateCamera(CameraUpdateFactory.newLatLng(it))

            Toast.makeText(this, "Lat: ${it.latitude}, Long: ${it.longitude}", Toast.LENGTH_LONG).show()


        }

    }

    /*private fun initRecyclerView() {
        Thread {
            var users = 1
            items = items.reversed()

            runOnUiThread {
                itemAdapter = ItemAdapter(this@MainActivity, items)

                recyclerItem.adapter = itemAdapter
                val callback = ItemTouchHelperCallback(itemAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerItem)
            }
        }.start()
    }*/
}