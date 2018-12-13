package com.example.aabid.gittogether

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.code_dialog.view.*
import java.lang.RuntimeException

class CodeDialog : DialogFragment() {

    interface GroupHandler {
        fun groupUpdated(uid: String)
    }

    private lateinit var groupHandler: GroupHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is GroupHandler) {
            groupHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.ground_handler_interface))
        }
    }


    private lateinit var etGroupCode: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.join_group))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.code_dialog, null
        )

        etGroupCode = rootView.etGroupCode

        builder.setView(rootView)

        builder.setPositiveButton(getString(R.string.done_caps)) { dialog, witch ->
            // empty
        }

        builder.setNegativeButton(getString(R.string.cancel_caps)) { dialog, which ->
            dialog.dismiss()
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etGroupCode.text.isNotEmpty()) {
                handleGroupUpdate()
                dialog.dismiss()
            } else {
                etGroupCode.error = getString(R.string.this_field_cannot)
            }
        }
    }

    private fun handleGroupUpdate() {
        groupHandler.groupUpdated(etGroupCode.text.toString())
    }





}