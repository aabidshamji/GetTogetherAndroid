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

        val newUser = User()

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
                getString(R.string.registration_ok), Toast.LENGTH_LONG).show()
            loginNewUser(newUser)

        }.addOnFailureListener{
            progressBar2.visibility = View.INVISIBLE
            Toast.makeText(this@AccountActivity,
                applicationContext.getString(R.string.register_error, it.message),Toast.LENGTH_LONG).show()
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
                Toast.makeText(this@AccountActivity, getString(R.string.error_adding_user_to_db), Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this@AccountActivity,
                applicationContext.getString(R.string.login_error, it.message),Toast.LENGTH_LONG).show()
        }

    }

    private fun addUserToDB(newUser : User) {

        val database = FirebaseDatabase.getInstance().reference

        newUser.groups.plusElement("default")

        database.child("users").child(newUser.uid).setValue(newUser).addOnSuccessListener {
            Toast.makeText(this, getString(R.string.done), Toast.LENGTH_LONG).show()
        }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show()
            }

    }

    private fun isFormValid(): Boolean {
        return when {
            etFirst.text.isEmpty() -> {
                etFirst.error = getString(R.string.this_field_cannot)
                false
            }
            etLast.text.isEmpty() -> {
                etLast.error = getString(R.string.this_field_cannot)
                false
            }
            etEmail.text.isEmpty() -> {
                etEmail.error = getString(R.string.this_field_cannot)
                false
            }
            etPassword.text.isEmpty() -> {
                etPassword.error = getString(R.string.this_field_cannot)
                false
            }
            etConfirm.text.isEmpty() -> {
                etConfirm.error = getString(R.string.this_field_cannot)
                false
            }
            etPassword.text.toString() != etConfirm.text.toString() -> {
                etConfirm.error = getString(R.string.password_does_not_match)
                false
            }
            else -> true
        }
    }

}
