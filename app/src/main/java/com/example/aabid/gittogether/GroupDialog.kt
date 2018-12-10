package com.example.aabid.gittogether

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.group_dialog.*
import kotlinx.android.synthetic.main.group_dialog.view.*

class GroupDialog : DialogFragment() {

    private lateinit var etGroupName: EditText

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

        return builder.create()
    }
}