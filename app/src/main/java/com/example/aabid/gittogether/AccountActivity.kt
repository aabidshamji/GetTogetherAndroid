package com.example.aabid.gittogether

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.aabid.gittogether.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_account.*
import com.google.firebase.auth.FirebaseUser



class AccountActivity : AppCompatActivity() {

    private lateinit var etEmailBox : EditText
    private lateinit var etPasswordBox : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        etEmailBox = etEmail
        etPasswordBox = etPassword

        btnRegister.setOnClickListener {
            btnRegisterClicked()
        }

    }

    private fun btnRegisterClicked() {

        if (!isFormValid()){
            return
        }

        progressBar2.visibility = View.VISIBLE

        var newUser = User()

        newUser.name = etFirst.text.toString() + " " + etLast.text.toString()

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
            loginNewUser(newUser)

        }.addOnFailureListener{
            progressBar2.visibility = View.INVISIBLE
            Toast.makeText(this@AccountActivity,
                "Register error ${it.message}", Toast.LENGTH_LONG).show()
        }

    }

    private fun loginNewUser(newUser : User) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            etEmailBox.text.toString(),
            etPasswordBox.text.toString()
        ).addOnSuccessListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                newUser.uid = user.uid
                addUserToDB(newUser)
                startActivity(
                    Intent(this@AccountActivity,
                        HomeActivity::class.java)
                )
            } else {
                Toast.makeText(this@AccountActivity, "Error Adding User to DB", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this@AccountActivity,
                "Login error ${it.message}",Toast.LENGTH_LONG).show()
        }

    }

    private fun addUserToDB(newUser : User) {

        var database = FirebaseDatabase.getInstance().reference

        newUser.groups.plusElement("default")

        database.child("users").child(newUser.uid).setValue(newUser).addOnSuccessListener {
            Toast.makeText(this, "DONE!", Toast.LENGTH_LONG).show()
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed :(", Toast.LENGTH_LONG).show()
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

    override fun onStop() {
        super.onStop()

    }
}
