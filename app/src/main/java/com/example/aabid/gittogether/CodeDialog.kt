package com.example.aabid.gittogether

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.EditText
import com.example.aabid.gittogether.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.code_dialog.view.*
import kotlinx.android.synthetic.main.group_dialog.view.*

class CodeDialog : DialogFragment() {

    private lateinit var etGroupCode: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Join Group")

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.code_dialog, null
        )

        etGroupCode = rootView.etGroupCode

        builder.setView(rootView)

        builder.setPositiveButton("DONE") { dialog, witch ->
            // empty
        }

        builder.setNegativeButton("CANCEL") { dialog, which ->
            dialog.dismiss()
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etGroupCode.text.isNotEmpty()) {
                // TODO join group here
                dialog.dismiss()
            } else {
                etGroupCode.error = "This field cannot be empty"
            }
        }
    }



}