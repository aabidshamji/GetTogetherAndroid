package com.example.aabid.gittogether

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.group_row_content.*

class CodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)

        val myIntent = intent
        tvGroupID.text = myIntent.getStringExtra("GROUPID")
    }
}
