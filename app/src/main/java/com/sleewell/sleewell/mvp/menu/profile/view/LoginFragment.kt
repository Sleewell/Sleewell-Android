package com.sleewell.sleewell.mvp.menu.profile.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
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
    private val scrollThreshold: Float = 10f
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
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, RegisterFragment())?.commit()
        }

        editPassword.setOnEditorActionListener { _, actionId, keyEvent ->
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.login(editName.text.toString(), editPassword.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {
                presenter.login(editName.text.toString(), editPassword.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("") // ADD !!!
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(root.context, gso)

        val signInButton: SignInButton = root.findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            activity?.startActivityForResult(signInIntent, 9001)
            mGoogleSignInClient.signOut()
        }

        MainActivity.sendTokenToSleewell = { token ->
            presenter.loginGoogle(token)
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
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, ProfileFragment()).commit()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
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
                    if (isOnClick && (abs(mDownX - event.x) > scrollThreshold
                                || abs(mDownY - event.y) > scrollThreshold)) {
                        isOnClick = false
                    }
                }
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

    private fun isDoneKeyPressed(actionId: Int, keyEvent: KeyEvent): Boolean {
        return (actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.action == KeyEvent.ACTION_DOWN
                && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}