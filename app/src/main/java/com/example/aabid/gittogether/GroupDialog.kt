package com.example.aabid.gittogether

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter
import com.example.aabid.gittogether.data.Group
import com.example.aabid.gittogether.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.group_dialog.*
import kotlinx.android.synthetic.main.group_dialog.view.*
import java.lang.RuntimeException

class GroupDialog : DialogFragment() {

    private lateinit var etGroupName: EditText
    private lateinit var etMembers: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var currUser: User

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New Group")

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.group_dialog, null
        )
        etGroupName = rootView.etGroupName

        builder.setView(rootView)

        builder.setPositiveButton("DONE") {
                dialog, witch -> // empty
        }

        builder.setNegativeButton("CANCEL") {
            dialog, which ->  dialog.dismiss()
        }

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val currUserListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                currUser = dataSnapshot.getValue<User>(User::class.java)!!

                Log.i("currUser uid", currUser.uid)
                Log.i("currUser name", currUser.name)
                Log.i("currUser groups", currUser.groups.toString())
                Log.i("currUser latitude", currUser.latitude.toString())
                Log.i("currUser latitude", currUser.longitude.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.e("loadPost:onCancelled", databaseError.toException().toString())
                // ...
            }
        }

        database.child("users").child(mAuth.currentUser!!.uid).addValueEventListener(currUserListener)


        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etGroupName.text.isNotEmpty()) {
                createGroup(etGroupName.text.toString())
                dialog.dismiss()
            } else {
                etGroupName.error = "This field cannot be empty"
            }
        }
    }

    private fun createGroup(str : String) {
        var newGroup = Group(founder = mAuth.currentUser?.uid.toString())

        newGroup.name = str

        newGroup.members.add(newGroup.founder)

        //database.child("groups").child(str).setValue(newGroup).addOnSuccessListener {

        var newGroupRef = database.child("groups").push().key!!

        newGroup.uid = newGroupRef

        database.child("groups").child(newGroupRef).setValue(newGroup).addOnSuccessListener {
            //Toast.makeText(context as HomeActivity, "DONE!", Toast.LENGTH_LONG).show()

        }
            .addOnFailureListener {
                Toast.makeText(context as HomeActivity, "Failed :(", Toast.LENGTH_LONG).show()
            }

        
        currUser.groups.add(newGroup.uid)

        database.child("users").child(mAuth.currentUser!!.uid).child("groups").setValue(currUser.groups)


        //database.child("users").child(newGroup.founder).child("groups")
    }
}