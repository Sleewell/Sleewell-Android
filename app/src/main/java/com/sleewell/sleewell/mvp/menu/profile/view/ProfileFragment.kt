package com.sleewell.sleewell.mvp.menu.profile.view

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.sleewell.SleewellApiTracker
import com.sleewell.sleewell.modules.keyboardUtils.hideSoftKeyboard
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.profile.contract.ProfileContract
import com.sleewell.sleewell.mvp.menu.profile.presenter.ProfilePresenter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.math.abs


class ProfileFragment : Fragment(), ProfileContract.View {
    //Context
    private lateinit var presenter: ProfileContract.Presenter
    private lateinit var root: View

    //View widgets
    private lateinit var progressWidget: ProgressBar

    //Touch Detection
    private var mDownX: Float = 0f
    private var mDownY = 0f
    private val SCROLL_THRESHOLD: Float = 10f
    private var isOnClick = false

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
            setupUI(root.findViewById(R.id.profileParent))
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

        usernameInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.updateProfileInformation()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        firstNameInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.updateProfileInformation()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {

                presenter.updateProfileInformation()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        lastNameInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.updateProfileInformation()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {

                presenter.updateProfileInformation()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        emailInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.updateProfileInformation()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {

                presenter.updateProfileInformation()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
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

    private fun setupUI(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                println("action: " + event.action.toString())
                println(isOnClick)
                if (event.action == MotionEvent.ACTION_DOWN) {
                    mDownX = event.x
                    mDownY = event.y
                    isOnClick = true
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    if (isOnClick) {
                        hideSoftKeyboard()
                    }
                }
                if (event.action == MotionEvent.ACTION_MOVE) {
                    if (isOnClick && (abs(mDownX - event.x) > SCROLL_THRESHOLD
                                || abs(mDownY - event.y) > SCROLL_THRESHOLD)) {
                        isOnClick = false
                    }
                }
                v.performClick()
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    override fun showToast(message: String) {
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun isDoneKeyPressed(actionId: Int, keyEvent: KeyEvent): Boolean {
        return (actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.action == KeyEvent.ACTION_DOWN
                && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
    }
}