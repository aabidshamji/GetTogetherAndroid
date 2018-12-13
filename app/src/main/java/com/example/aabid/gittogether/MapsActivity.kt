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

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.group_member_row_content.view.*
import java.util.*
import android.util.Log
import android.view.Menu
import com.example.aabid.gittogether.data.Group


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private  lateinit var database : DatabaseReference
    private lateinit var groups: MutableList<Locations>
    private lateinit var membersList: Array<String>
    private var lat = 200.0
    private var long = 200.0
    private val users = mutableListOf<User>()//MutableList(4) {User("1", "Sanah",2,2)}
    private lateinit var mapActivityAdapter: MapActivityAdapter
    private lateinit var groupID : String
    private lateinit var groupObj : Group
    private lateinit var mCurrGroupReference : DatabaseReference
    private lateinit var mUsersReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        //TODO This could cause issues.  If code crashes check this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        database = FirebaseDatabase.getInstance().reference

        val intentMaps = intent
        val groupName = intentMaps.getStringExtra("GROUP_NAME")

        val currUserID = intentMaps.getIntExtra("USER", 0)

        tvGroupName.text = applicationContext.getString(R.string.group_name, groupName.toString())

        groupID = intentMaps.getStringExtra("GROUP_ID")

        groupObj = Group()

        mCurrGroupReference = database.child("groups").child(groupID)
        mUsersReference = database.child("users")

        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.maps_menu, menu)
        return true
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
                    users[j].longitude = 200.0
                }
            }
            if(members.size > 2) {
                var lat = 0.0
                var long = 0.0
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
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        //TODO Update users location on location changed
        //TODO add users here
       // val users = MutableList(2) {User("1", "Sanah",2,2),
        //User("2", "Adam", 2, 2),
        //User("3", "Aabid", 2,2)}

        groups = neighbor(users)
        for(i in 0 until groups.size) {
            val location = groups[i]
            val latitude = location.latitude.toDouble()
            val longitude = location.longitude.toDouble()
            val gc = Geocoder(this, Locale.getDefault())
            var addrs: List<Address>? = gc.getFromLocation(latitude, longitude, 1)
            mMap.addMarker(MarkerOptions().position(LatLng(groups[i].latitude.toDouble(),groups[i].longitude.toDouble())).title("${groups[i].size} members at ${addrs?.get(0)}"))
        }

        mMap.setOnMarkerClickListener {
            lat = it.position.latitude
            long = it.position.longitude
            val gc = Geocoder(this, Locale.getDefault())
            val addrs: List<Address>? = gc.getFromLocation(lat, long, 1)
            btnDirections.text = applicationContext.getString(R.string.directions, addrs?.get(0))

            false
        }
    }

    fun directions(v: View) {
        if(lat > 180.0) {
            Toast.makeText(this, "No location selected", Toast.LENGTH_LONG).show()
        }
        else {
            val uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, long)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            this.startActivity(intent)
        }
    }


    private fun initRecyclerView() {
        /*Thread {

            runOnUiThread {
                mapActivityAdapter = MapActivityAdapter(this@MapsActivity, users)

                recyclerViewNames.adapter = mapActivityAdapter
            }
        }.start()*/

        mapActivityAdapter = MapActivityAdapter(this@MapsActivity, users)

        recyclerViewNames.adapter = mapActivityAdapter

        startGetUsers()

        /*for (u in users) {
            mapActivityAdapter.addGroupMembers(u)
        } */
    }

    private fun getGroupMembers() {

        mUsersReference.addChildEventListener(object:ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.i("onChildCancelled", "Cancelled")            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.i("onChildMoved", "Moved")            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(User::class.java)

                if (groupObj.members.contains(user?.uid)) {

                    mapActivityAdapter.groupMembersItems

                    for (dude in mapActivityAdapter.groupMembersItems) {
                        if (dude.uid == user!!.uid) {
                            dude.groups = user.groups
                            dude.uid = user.uid
                            dude.name = user.name
                            dude.longitude = user.longitude
                            dude.latitude = user.latitude
                        }
                    }

                    Log.d("TAGDDD ADDED", user?.name)

                    Log.d("T addingUser name", user?.name)
                    Log.d("T addingUser uid", user?.uid)
                    Log.d("T addingUser members", user?.groups.toString())

                } else {
                    Log.d("TAGDDD NOT-ADDED",user?.name)
                }


            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                Log.d("MAPS T BEFORE", "DONDEONDEON")

                val user = p0.getValue(User::class.java)

                Log.d("MAPS T GROUPLIST", groupObj.members.toString())
                Log.d("MAPS T UID", user?.uid)

                if (groupObj.members.contains(user?.uid)) {
                    mapActivityAdapter.addGroupMembers(user!!)

                    Log.d("TAGDDD ADDED", user?.name)

                    Log.d("T addingUser name", user?.name)
                    Log.d("T addingUser uid", user?.uid)
                    Log.d("T addingUser members", user?.groups.toString())

                } else {
                    Log.d("TAGDDD NOT-ADDED",user?.name)
                }

                //p0.key

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

    private fun startGetUsers(){
        val currGroupListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                groupObj = dataSnapshot.getValue<Group>(Group::class.java)!!

                Log.i("GroupObj uid", groupObj.uid)
                Log.i("GroupObj founder", groupObj.founder)
                Log.i("GroupObj name", groupObj.name)
                Log.i("GroupObj members", groupObj.members.toString())
                Log.i("GroupObj latitude", groupObj.latitude.toString())
                Log.i("GroupObj latitude", groupObj.longitude.toString())

                getGroupMembers()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.e("loadPost:onCancelled", databaseError.toException().toString())
                // ...
            }
        }

        mCurrGroupReference.addValueEventListener(currGroupListener)

    }

    fun goToProfile(v: View) {
        val intentStart = Intent()
        intentStart.setClass((this@MapsActivity),
            ProfileActivity::class.java)
        val username = v.tvUserName.text.toString()
        intentStart.putExtra("USER_NAME", username)
        startActivity(intentStart)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_leave -> {
                //TODO Need to remove user with given id
            }
            R.id.action_refresh -> {
                //TODO Need to update firebase information
            }
        }
        return when (item.itemId) {
            R.id.action_leave -> true
            R.id.action_refresh -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}