package com.sleewell.sleewell.mvp.menu.profile.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sleewell.sleewell.R
import kotlinx.android.synthetic.main.profile_bottom_sheet_modal.*

class ProfileBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile_bottom_sheet_modal, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        item1.setOnClickListener {
            //handle click event
            Toast.makeText(context, "First Button Clicked", Toast.LENGTH_SHORT).show()
        }
        item2.setOnClickListener {
            //handle click event
            Toast.makeText(context, "Third Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG = "ProfileBottomSheet"
    }
}