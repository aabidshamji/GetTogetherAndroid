package com.example.aabid.gittogether

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.aabid.gittogether.data.Locations
import com.example.aabid.gittogether.data.User
import com.example.aabid.gittogether.mapactivity.MapActivityAdapter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.group_member_row_content.view.*
import java.util.*
import android.support.v4.content.ContextCompat.startActivity



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private  lateinit var database : DatabaseReference
    private lateinit var mapActivityAdapter: MapActivityAdapter
    private lateinit var groups: MutableList<Locations>
    private var lat = 200.0
    private var long = 200.0

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
    private fun neighbor(users: MutableList<User>): MutableList<Locations> {
        var groups = mutableListOf<Locations>()
        for(i in 0 until users.size) {
            var members = mutableListOf<User>()
            members.add(users[i])
            for(j in i until users.size) {
                if((users[i].latitude-1)<users[j].latitude && users[j].latitude<(users[i].latitude+1)
                    && (users[i].longitude-1)<users[j].longitude && users[j].longitude<(users[i].longitude+1)) {
                    members.add(users[j])
                    users[j].longitude = 200
                }
            }
            if(members.size > 2) {
                var lat = 0
                var long = 0
                for(member in members) {
                    lat += member.latitude
                    long += member.longitude
                }
                groups.add(Locations(lat/members.size, long/members.size, members.size))
            }
        }
        return groups
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //TODO add users here
        val users = MutableList(2) {User("", "",2,2)}
        groups = neighbor(users)
        for(i in 0 until groups.size) {
            val location = groups[i]
            val latitude = location.latitude.toDouble()
            val longitude = location.longitude.toDouble()
            val gc = Geocoder(this, Locale.getDefault())
            var addrs: List<Address>? = gc.getFromLocation(latitude, longitude, 1)
            mMap.addMarker(MarkerOptions().position(LatLng(groups[i].latitude.toDouble(),groups[i].longitude.toDouble())).title("${groups[i].size} members at ${addrs?.get(0)}"))
        }

        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.setOnMapClickListener { it ->
            val markerOpt = MarkerOptions()
                .position(it)
                .title("My Marker ${it.latitude}, ${it.longitude}")

            val markerNew = mMap.addMarker(markerOpt)

            markerNew.isDraggable = true

            mMap.animateCamera(CameraUpdateFactory.newLatLng(it))

            Toast.makeText(this, "Lat: ${it.latitude}, Long: ${it.longitude}", Toast.LENGTH_LONG).show()

            mMap.setOnMarkerClickListener() {
                lat = it.position.latitude
                long = it.position.longitude
                val gc = Geocoder(this, Locale.getDefault())
                var addrs: List<Address>? = gc.getFromLocation(lat, long, 1)
                btnDirections.text = "Get directions to ${addrs?.get(0)}"

                false
            }

        }
    }

    fun directions(v: View) {
        if(lat == 200.0) {
            Toast.makeText(this, "No location selected", Toast.LENGTH_LONG).show()
        }
        else {
            val uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, long)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            this.startActivity(intent)
        }
    }



    /*private fun initRecyclerView() {
        Thread {
            var users =
            users = users.reversed()

            runOnUiThread {
                mapActivityAdapter = MapActivityAdapter(this@MapsActivity, users)

                recyclerViewNames.adapter = mapActivityAdapter
                val callback = TouchHelperCallback(mapActivityAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerViewNames)
            }
        }.start()
    }*/

    fun goToProfile(v: View) {
        val intentStart = Intent()
        intentStart.setClass((this@MapsActivity),
            MapsActivity::class.java)
        val username = v.tvUserName.text.toString()
        intentStart.putExtra("USER_NAME", username)
        startActivity(intentStart)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_leave -> {
                //TODO Need to delete group member whose id matches users id and remove from recycler
            }
        }
        return when (item.itemId) {
            R.id.action_leave -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}