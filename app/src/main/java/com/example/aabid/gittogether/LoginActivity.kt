package com.example.aabid.gittogether

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.view.View
import com.google.firebase.auth.UserProfileChangeRequest


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun registerClick(v: View) {

        startActivity(Intent(this@LoginActivity,
            AccountActivity::class.java))

    }

    fun loginClick(v: View) {

        if (!isFormValid()) {
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            etEmail.text.toString(),
            etPassword.text.toString()
        ).addOnSuccessListener {
            startActivity(Intent(this@LoginActivity,
                MainActivity::class.java))
        }.addOnFailureListener{
            Toast.makeText(this@LoginActivity,
                "Login error ${it.message}",Toast.LENGTH_LONG).show()
        }


    }

    private fun isFormValid(): Boolean {
        return when {
            etEmail.text.isEmpty() -> {
                etEmail.error = "This field can not be empty"
                false
            }
            etPassword.text.isEmpty() -> {
                etPassword.error = "This field can not be empty"
                false
            }
            else -> true
        }
    }
}
