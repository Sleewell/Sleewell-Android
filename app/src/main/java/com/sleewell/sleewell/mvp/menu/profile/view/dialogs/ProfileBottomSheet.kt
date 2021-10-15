package com.sleewell.sleewell.mvp.menu.profile.view.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sleewell.sleewell.R
import kotlinx.android.synthetic.main.profile_bottom_sheet_modal.*

class ProfileBottomSheet : BottomSheetDialogFragment() {

    private lateinit var eventListener: DialogEventListener

    interface DialogEventListener {
        fun onItem1Click()
        fun onItem2Click()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile_bottom_sheet_modal, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        item1.setOnClickListener {
            eventListener.onItem1Click()
        }
        item2.setOnClickListener {
            eventListener.onItem2Click()
        }
    }

    companion object {
        const val TAG = "ProfileBottomSheet"
    }
}