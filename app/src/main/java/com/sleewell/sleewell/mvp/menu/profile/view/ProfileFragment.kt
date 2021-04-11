package com.sleewell.sleewell.mvp.menu.profile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.profile.ProfileContract
import com.sleewell.sleewell.mvp.menu.profile.presenter.ProfilePresenter

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), ProfileContract.View {
    //Context
    private lateinit var presenter: ProfileContract.Presenter
    private lateinit var root: View

    //View widgets

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false)
        initActivityWidgets()
        setPresenter(ProfilePresenter(this, this.activity as AppCompatActivity))

        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {

    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }
}