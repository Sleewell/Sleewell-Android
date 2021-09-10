package com.sleewell.sleewell.mvp.menu.profile.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.sleewell.sleewell.R

class GivenImagesDialog : DialogFragment(), View.OnClickListener {
    private lateinit var eventListener: DialogEventListener

    private lateinit var image1: ImageView
    private lateinit var image2: ImageView

    interface DialogEventListener {
        fun onDialogPictureClick(picture: ImageView)
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
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_profile_given_images, null)
            builder.setView(dialogView)

            image1 = dialogView.findViewById(R.id.image1)
            image2 = dialogView.findViewById(R.id.image2)

            image1.setOnClickListener(this)
            image2.setOnClickListener(this)

            builder.create()
        }
    }

    override fun onClick(view: View?) {
        if (view is ImageView) {
            eventListener.onDialogPictureClick(view)
            this.dismiss()
        }
    }
}