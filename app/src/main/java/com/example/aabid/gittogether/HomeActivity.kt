package com.example.aabid.gittogether

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.aabid.gittogether.R.id.home
import com.example.aabid.gittogether.data.Group
import com.example.aabid.gittogether.data.User
import com.example.aabid.gittogether.groupadapter.HomeActivityAdapter
import com.example.aabid.gittogether.mapactivity.MyLocationProvider
import com.example.aabid.gittogether.touch.TouchHelperCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.group_row_content.view.*
import kotlinx.android.synthetic.main.nav_header_home.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.group_row.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MyLocationProvider.OnNewLocationAvailable, CodeDialog.GroupHandler {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var myLocationProvider: MyLocationProvider
    private lateinit var mCurrUserReference: DatabaseReference
    private lateinit var mGroupsReference: DatabaseReference
    private lateinit var currUser : User
    private lateinit var groupsList : MutableList<Group>
    private lateinit var homeActivityAdapter : HomeActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            showAddGroupDialog()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        mCurrUserReference = database.child("users").child(mAuth.currentUser!!.uid)
        mGroupsReference = database.child("groups")
        currUser = User()
        groupsList = mutableListOf()


        initRecyclerView()

    }

    fun startGetGroups(){
        val currUserListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                currUser = dataSnapshot.getValue<User>(User::class.java)!!

                Log.i("currUser uid", currUser.uid)
                Log.i("currUser name", currUser.name)
                Log.i("currUser groups", currUser.groups.toString())
                Log.i("currUser latitude", currUser.latitude.toString())
                Log.i("currUser latitude", currUser.longitude.toString())

                getGroups()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.e("loadPost:onCancelled", databaseError.toException().toString())
            }
        }

        mCurrUserReference.addValueEventListener(currUserListener)
    }

    fun getGroups(){
        mGroupsReference.addChildEventListener(object:ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val group = p0.getValue(Group::class.java)

                Log.d("TAGDDD GROUPLIST", currUser.groups.toString())
                Log.d("TAGDDD NEW UID", group?.uid)
                var newList = mutableListOf<Group>()
                if (currUser.groups.contains(group?.uid)) {
                    homeActivityAdapter.addGroup(group!!)
                    Log.d("TAGDDD ADDED", group?.name)

                    Log.d("T addingGroup founder", group?.founder)
                    Log.d("T addingGroup name", group?.name)
                    Log.d("T addingGroup uid", group?.uid)
                    Log.d("T addingGroup members", group?.members.toString())

                } else {
                    Log.d("TAGDDD NOT-ADDED",group?.name)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

    }

    override fun onStart() {
        super.onStart()
        startLocation()
    }

    @WithPermissions(
        permissions = [android.Manifest.permission.ACCESS_FINE_LOCATION]
    )
    fun startLocation() {
        myLocationProvider = MyLocationProvider(this,
            this)
        myLocationProvider.startLocationMonitoring()
    }


    override fun onNewLocation(location: Location) {
        setLocation(location)
    }

    override fun groupUpdated(uid: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setLocation(location: Location) {
        database.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("latitude").setValue(location.latitude)
        database.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("longitude").setValue(location.longitude)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        val currUser = mAuth.currentUser

        if (currUser?.displayName != null) {
            tvMenuName.text = currUser.displayName.toString()
            tvMenuEmail.text = currUser.email.toString()
        } else {
            tvMenuName.text = "Error"
            tvMenuEmail.text = "Error"
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_create_group -> {
                showAddGroupDialog()
            }
            R.id.nav_join_group -> {
                //TODO Need to open a dialog that allows you to type in group ID
            }
            R.id.nav_profile -> {
                startActivity(
                    Intent(this@HomeActivity,
                        ProfileActivity::class.java)
                )
            }
            R.id.nav_share -> {
                shareEmail()
            }
            R.id.nav_logout -> {
                logout()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    private fun shareEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Join This Awesome App!!")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "You should download this app called get together. It is really cool!")
        startActivity(Intent.createChooser(emailIntent, "Send mail..."))
    }

    private fun logout() {
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        startActivity(
            Intent(
                this@HomeActivity,
                LoginActivity::class.java
            )
        )
    }

    private fun initRecyclerView() {
        homeActivityAdapter = HomeActivityAdapter(this@HomeActivity, groupsList)
        recycler_groups.adapter = homeActivityAdapter

        //getGroups()
        startGetGroups()
    }



    private fun showAddGroupDialog() {
        GroupDialog().show(supportFragmentManager,
            "TAG_CREATE")
    }

    fun groupDetails(v: View) {
        val intentStart = Intent()
        intentStart.setClass((this@HomeActivity),
            MapsActivity::class.java)
        val group = v.tvGroupName.text.toString()

        //TODO Currently taking the group name but need the group id
        intentStart.putExtra("GROUP_NAME", group)
        intentStart.putExtra("GROUP_ID", v.tvGroupID.text.toString())
        intentStart.putExtra("USER", currUser.uid)
        startActivity(intentStart)
    }

    override fun onDestroy() {
        super.onDestroy()
        myLocationProvider.stopLocationMonitoring()
    }
}
