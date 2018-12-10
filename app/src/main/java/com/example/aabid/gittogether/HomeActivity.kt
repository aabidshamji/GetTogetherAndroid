package com.example.aabid.gittogether

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.aabid.gittogether.data.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.group_row_content.view.*
import kotlinx.android.synthetic.main.nav_header_home.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

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
        menuInflater.inflate(R.menu.home, menu)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_maps -> {
                startActivity(
                    Intent(this@HomeActivity,
                        MapsActivity::class.java)
                )
            }
            R.id.nav_create_group -> {

                var newGroup = Group(founder = mAuth.currentUser?.uid.toString())

                newGroup.members.add(0, "1")
                newGroup.members.add(0, "2")
                newGroup.members.add(0, "3")

                database.child("groups").child("testGroup").setValue(newGroup).addOnSuccessListener {
                    Toast.makeText(this, "DONE!", Toast.LENGTH_LONG).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed :(", Toast.LENGTH_LONG).show()
                    }
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
            R.id.nav_settings -> {
                showAddGroupDialog()
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

    private fun showAddGroupDialog() {
        GroupDialog().show(supportFragmentManager,
            "TAG_CREATE")
    }

    fun groupDetails(v: View) {
        val intentStart = Intent()
        intentStart.setClass((this@HomeActivity),
            MapsActivity::class.java)
        val group = v.tvGroupName.text.toString()
        intentStart.putExtra("GROUP_NAME", group)
        startActivity(intentStart)
    }
}
