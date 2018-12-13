package com.example.aabid.gittogether

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.view.View


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

        progressBar.visibility = View.VISIBLE

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            etEmail.text.toString(),
            etPassword.text.toString()
        ).addOnSuccessListener {
            progressBar.visibility = View.INVISIBLE
            startActivity(Intent(this@LoginActivity,
                HomeActivity::class.java))
        }.addOnFailureListener{
            progressBar.visibility = View.INVISIBLE
            Toast.makeText(this@LoginActivity,
                applicationContext.getString(R.string.login_error, it.message),Toast.LENGTH_LONG).show()
        }


    }

    private fun isFormValid(): Boolean {
        return when {
            etEmail.text.isEmpty() -> {
                etEmail.error = getString(R.string.this_field_cannot)
                false
            }
            etPassword.text.isEmpty() -> {
                etPassword.error = getString(R.string.this_field_cannot)
                false
            }
            else -> true
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}
