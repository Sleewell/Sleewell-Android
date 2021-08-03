package com.sleewell.sleewell.mvp.menu.profile.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.keyboardUtils.hideSoftKeyboard
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.profile.contract.RegisterContract
import com.sleewell.sleewell.mvp.menu.profile.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.new_fragment_resgister_api.*
import kotlin.math.abs

class RegisterFragment : Fragment(), RegisterContract.View {
    private lateinit var presenter: RegisterContract.Presenter
    private lateinit var root: View

    private val emailRegex =
        "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

    private lateinit var editLoginId: EditText
    private lateinit var editPassword_1: EditText
    private lateinit var editPassword_2: EditText
    private lateinit var editEmail: EditText
    private lateinit var editFirstName: EditText
    private lateinit var editLastName: EditText
    private lateinit var signUpButton: ImageView
    private lateinit var loginButton: TextView

    //Touch Detection
    private var mDownX: Float = 0f
    private var mDownY = 0f
    private val SCROLL_THRESHOLD: Float = 10f
    private var isOnClick = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        root = inflater.inflate(R.layout.new_fragment_resgister_api, container, false)

        setPresenter(RegisterPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        setupUI(root.findViewById(R.id.registerParent))

        editLoginId = root.findViewById(R.id.editLoginId)
        editPassword_1 = root.findViewById(R.id.editPassword_1)
        editPassword_2 = root.findViewById(R.id.editPassword_2)
        editEmail = root.findViewById(R.id.editEmail)
        editFirstName = root.findViewById(R.id.editFirstName)
        editLastName = root.findViewById(R.id.editLastName)
        signUpButton = root.findViewById(R.id.buttonSignUp)
        loginButton = root.findViewById(R.id.textLogin)

        signUpButton.setOnClickListener {
            if (editLoginId.text.length < 5) {
                displayToast("Error : invalid login, must contains at least 5 characters")
                return@setOnClickListener
            }
            if (!checkPassword(editPassword_1.text.toString())) {
                displayToast("Error : password must contains 8 characters and at least 1 number")
                return@setOnClickListener
            }
            if (editPassword_1.text.toString() != editPassword_2.text.toString()) {
                displayToast("Error : password are not identical")
                return@setOnClickListener
            }
            if (!Regex(emailRegex).matches(editEmail.text)) {
                displayToast("Error : email invalid")
                return@setOnClickListener
            }
            if (editFirstName.text.isEmpty() || editLastName.text.isEmpty()) {
                displayToast("Error : First Name or Last Name cannot be empty")
                return@setOnClickListener
            }
            presenter.register(
                editLoginId.text.toString(),
                editPassword_1.text.toString(),
                editEmail.text.toString(),
                editFirstName.text.toString(),
                editLastName.text.toString()
            )

            displayLoading()
        }

        loginButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, LoginFragment())?.commit()
        }
        return root
    }

    private fun checkPassword(pw: String): Boolean {
        var containsDigit = false
        var containsLetter = false

        if (pw.length < 8)
            return false
        pw.forEach {
            if (it.isDigit())
                containsDigit = true
            if (it.isLetter())
                containsLetter = true
        }
        return containsLetter && containsDigit
    }

    override fun displayLoading() {
        val inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = 200

        progressBarHolder?.animation = inAnimation
        progressBarHolder?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        val outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.duration = 200

        progressBarHolder?.animation = outAnimation
        progressBarHolder?.visibility = View.GONE
    }

    override fun setAccessToken(token: String) {
        val sharedPref =
            activity?.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE)
        with(sharedPref?.edit()) {
            this?.putString(getString(R.string.user_token_key), token)
            this?.apply()
        }
        MainActivity.accessTokenSleewell = token
        fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, ProfileFragment())?.commit()
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

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: RegisterContract.Presenter) {
        this.presenter = presenter
    }
}