package com.example.aabid.gittogether

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.group_dialog.*
import kotlinx.android.synthetic.main.group_dialog.view.*
import java.lang.RuntimeException

class GroupDialog : DialogFragment() {

    private lateinit var etGroupName: EditText
    private lateinit var etMembers: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

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

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etGroupName.text.isNotEmpty()) {
                createGroup(etGroupName.text.toString(), getNames(etMembers.text.toString()))
                dialog.dismiss()
            } else {
                etGroupName.error = "This field cannot be empty"
            }
        }
    }

    private fun createGroup(str : String, frnds : List<String>) {
        var newGroup = Group(founder = mAuth.currentUser?.uid.toString())

        newGroup.name = str
        for (i in 0 until frnds.size) {
            newGroup.members.add(i, frnds[i]);
        }

        database.child("groups").child(str).setValue(newGroup).addOnSuccessListener {
            //Toast.makeText(context as HomeActivity, "DONE!", Toast.LENGTH_LONG).show()
        }
            .addOnFailureListener {
                Toast.makeText(context as HomeActivity, "Failed :(", Toast.LENGTH_LONG).show()
            }
    }

    private fun getNames(str : String) : List<String> {
        var lst =  str.split(",".toRegex());
        return lst;
    }
}