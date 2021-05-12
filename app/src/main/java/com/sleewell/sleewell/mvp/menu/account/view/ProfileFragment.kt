package com.sleewell.sleewell.mvp.menu.account.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.sleewell.SleewellApiTracker
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.account.contract.ProfileContract
import com.sleewell.sleewell.mvp.menu.account.presenter.ProfilePresenter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileContract.View {
    //Context
    private lateinit var presenter: ProfileContract.Presenter
    private lateinit var root: View

    //View widgets
    private lateinit var progressWidget: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false)

        if (MainActivity.accessTokenSleewell.isEmpty()) {
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, LoginFragment())?.commit()
        } else {
            initActivityWidgets()
            setPresenter(ProfilePresenter(this, this.activity as AppCompatActivity))
        }
        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        val usernameInputWidget = root.findViewById<TextInputLayout>(R.id.usernameInputLayout)
        val firstNameInputWidget = root.findViewById<TextInputLayout>(R.id.firstNameInputLayout)
        val lastNameInputWidget = root.findViewById<TextInputLayout>(R.id.lastNameInputLayout)
        val emailInputWidget = root.findViewById<TextInputLayout>(R.id.emailInputLayout)
        progressWidget = root.findViewById(R.id.progress)

        val saveButtonWidget = root.findViewById<ImageButton>(R.id.buttonSave)
        val logoutButtonWidget = root.findViewById<ImageButton>(R.id.buttonLogout)

        saveButtonWidget.setOnClickListener {
            presenter.updateProfileInformation()
        }
        logoutButtonWidget.setOnClickListener {
            context?.let { it1 -> SleewellApiTracker.disconnect(it1) }
            MainActivity.accessTokenSleewell = ""
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, LoginFragment())?.commit()
        }

        usernameInputWidget.editText?.doOnTextChanged { input, _, _, _ ->
            presenter.setUsername(input.toString())
        }
        firstNameInputWidget.editText?.doOnTextChanged { input, _, _, _ ->
            presenter.setFirstName(input.toString())
        }
        lastNameInputWidget.editText?.doOnTextChanged { input, _, _, _ ->
            presenter.setLastName(input.toString())
        }
        emailInputWidget.editText?.doOnTextChanged { input, _, _, _ ->
            presenter.setEmail(input.toString())
        }
    }

    override fun updateProfileInfoWidgets(username: String, firstName: String, lastName: String, email: String) {
        usernameInputLayout?.editText?.setText(username)
        firstNameInputLayout?.editText?.setText(firstName)
        lastNameInputLayout?.editText?.setText(lastName)
        emailInputLayout?.editText?.setText(email)

        usernameInputLayout?.editText?.visibility = View.VISIBLE
        firstNameInputLayout?.editText?.visibility = View.VISIBLE
        lastNameInputLayout?.editText?.visibility = View.VISIBLE
        emailInputLayout?.editText?.visibility = View.VISIBLE

        usernameInputLayout?.visibility = View.VISIBLE
        firstNameInputLayout?.visibility = View.VISIBLE
        lastNameInputLayout?.visibility = View.VISIBLE
        emailInputLayout?.visibility = View.VISIBLE

        progressWidget.visibility = View.GONE
    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    override fun showToast(message: String) {
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
    }
}