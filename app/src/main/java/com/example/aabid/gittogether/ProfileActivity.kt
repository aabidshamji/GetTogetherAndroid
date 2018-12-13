package com.example.aabid.gittogether

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val intentMaps = intent
        val username = intentMaps.getStringExtra("USER_NAME")

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val currUser = mAuth.currentUser

        if (currUser?.displayName != null) {
            if(username != null){
                tvName.text = username
                tvEmail.visibility = View.INVISIBLE
            }
            else {
                tvName.text = currUser.displayName.toString()
                tvEmail.text = currUser.email.toString()
            }
        } else {
            tvName.text = getString(R.string.error)
            tvEmail.text = getString(R.string.error)
        }
    }
}
