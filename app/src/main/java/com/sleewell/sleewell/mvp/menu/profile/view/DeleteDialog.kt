package com.sleewell.sleewell.mvp.menu.profile.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteDialog : DialogFragment() {
    private lateinit var eventListener: DialogEventListener

    interface DialogEventListener {
        fun onContinue()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            eventListener = context as DialogEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement DialogEventListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Account")
                .setMessage(
                    "The following action will permanently delete your account.\n" +
                    "Are you sure you wish to continue?")
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                    eventListener.onContinue()
                }.create()
        }
    }
}