package com.sleewell.sleewell.mvp.menu.profile.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PickImageDialog : DialogFragment() {
    // Use this instance of the interface to deliver action events
    private lateinit var eventListener: DialogEventListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface DialogEventListener {
        fun onDialogTakePictureClick(dialog: DialogFragment)
        fun onDialogPickPictureClick(dialog: DialogFragment)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            eventListener = context as DialogEventListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            AlertDialog.Builder(requireContext())
                .setItems( arrayOf(
                    "Take a picture",
                    "Pick from gallery"))
                { dialog, which ->
                    dialog.dismiss()
                    when (which) {
                        0 -> eventListener.onDialogTakePictureClick(this)
                        1 -> eventListener.onDialogPickPictureClick(this)
                    }
                }.create()
        }
    }

}