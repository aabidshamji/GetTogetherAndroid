package com.example.aabid.gittogether

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        btnRegister.setOnClickListener {
            btnRegisterClicked()
        }



    }

    private fun btnRegisterClicked() {

        if (!isFormValid()){
            return
        }

        progressBar2.visibility = View.VISIBLE

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            etEmail.text.toString(), etPassword.text.toString()
        ).addOnSuccessListener {
            val user = it.user
            user.updateProfile(
                UserProfileChangeRequest.Builder().
                    setDisplayName(etFirst.text.toString() + " " + etLast.text.toString())
                    .build()
            )
            progressBar2.visibility = View.INVISIBLE
            Toast.makeText(this@AccountActivity,
                "Registration OK", Toast.LENGTH_LONG).show()

        }.addOnFailureListener{
            progressBar2.visibility = View.INVISIBLE
            Toast.makeText(this@AccountActivity,
                "Register error ${it.message}", Toast.LENGTH_LONG).show()
        }

    }



    private fun isFormValid(): Boolean {
        return when {
            etFirst.text.isEmpty() -> {
                etFirst.error = "This field can not be empty"
                false
            }
            etLast.text.isEmpty() -> {
                etLast.error = "This field can not be empty"
                false
            }
            etEmail.text.isEmpty() -> {
                etEmail.error = "This field can not be empty"
                false
            }
            etPassword.text.isEmpty() -> {
                etPassword.error = "This field can not be empty"
                false
            }
            etConfirm.text.isEmpty() -> {
                etConfirm.error = "This field can not be empty"
                false
            }
            etPassword.text.toString() != etConfirm.text.toString() -> {
                etConfirm.error = "Password does not match"
                false
            }
            else -> true
        }
    }
}
