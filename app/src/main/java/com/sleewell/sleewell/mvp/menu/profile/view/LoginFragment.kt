package com.sleewell.sleewell.mvp.menu.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.sleewell.SleewellApiTracker
import com.sleewell.sleewell.modules.keyboardUtils.hideSoftKeyboard
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.profile.contract.LoginContract
import com.sleewell.sleewell.mvp.menu.profile.presenter.LoginPresenter
import kotlinx.android.synthetic.main.new_fragment_resgister_api.*
import kotlin.math.abs


class LoginFragment : Fragment(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter
    private lateinit var root: View

    private lateinit var loginButton: ImageView
    private lateinit var signUpButton: TextView
    private lateinit var editName: EditText
    private lateinit var editPassword: EditText

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
        root = inflater.inflate(R.layout.new_fragment_connection_api, container, false)

        setPresenter(LoginPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        setupUI(root.findViewById(R.id.connectionParent))

        loginButton = root.findViewById(R.id.loginImageLogin)
        signUpButton = root.findViewById(R.id.loginTextSignup)
        editName = root.findViewById(R.id.editTextName)
        editPassword = root.findViewById(R.id.editTextPassword)
        loginButton.setOnClickListener {
            presenter.login(editName.text.toString(), editPassword.text.toString())
            displayLoading()
        }
        signUpButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, RegisterFragment())?.commit()
        }
        return root
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
        context?.let { SleewellApiTracker.setToken(it, token) }
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

    override fun setPresenter(presenter: LoginContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}