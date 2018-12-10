package com.example.aabid.gittogether

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val currUser = mAuth.currentUser

        if (currUser?.displayName != null) {
            tvName.text = currUser.displayName.toString()
            tvEmail.text = currUser.email.toString()
        } else {
            tvName.text = "Error"
            tvEmail.text = "Error"
        }
    }
}
