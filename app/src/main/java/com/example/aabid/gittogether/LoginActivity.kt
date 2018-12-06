package com.example.aabid.gittogether

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            var myIntent = Intent()
            myIntent.setClass(this@LoginActivity, MainActivity::class.java)
            startActivity(myIntent)
        }

        btnRegister.setOnClickListener {
            var myIntent = Intent()
            myIntent.setClass(this@LoginActivity, AccountActivity::class.java)
            startActivity(myIntent)
        }
    }
}
