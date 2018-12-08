package com.example.aabid.gittogether

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun mapClick (v: View) {
        startActivity(
            Intent(this@MainActivity,
                MapsActivity::class.java)
        )
    }
}
