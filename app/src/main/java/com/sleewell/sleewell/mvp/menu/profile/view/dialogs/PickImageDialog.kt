package com.sleewell.sleewell.mvp.menu.profile.view.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sleewell.sleewell.mvp.menu.profile.view.ProfileFragment


class PickImageDialog : DialogFragment() {
    // Use this instance of the interface to deliver action events
    private lateinit var eventListener: DialogEventListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface DialogEventListener {
        fun onDialogTakePictureClick(dialog: DialogFragment)
        fun onDialogPickPictureClick(dialog: DialogFragment)
        fun onDialogGivenPictureClick(dialog: DialogFragment)
    }

    // Override the Fragment.onAttach() method to instantiate the DialogEventListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DialogEventListener so we can send events to the host
            eventListener = context as DialogEventListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement DialogEventListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            MaterialAlertDialogBuilder(requireContext())
                .setItems(arrayOf(
                    "Take a picture",
                    "Pick from gallery",
                    "Choose from given pictures"))
                { _, which ->
                    this.dismiss()
                    when (which) {
                        0 -> eventListener.onDialogTakePictureClick(this)
                        1 -> eventListener.onDialogPickPictureClick(this)
                        2 -> eventListener.onDialogGivenPictureClick(this)
                    }
                }.create()
        }
    }

    override fun dismiss() {
        super.dismiss()
        ProfileFragment.flagPickDialog = true
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        ProfileFragment.flagPickDialog = true
    }
}