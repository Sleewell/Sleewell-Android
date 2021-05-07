package com.sleewell.sleewell.mvp.menu.account.view

import android.os.Bundle
import android.view.LayoutInflater
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
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.account.contract.RegisterContract
import com.sleewell.sleewell.mvp.menu.account.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.new_fragment_resgister_api.*


class RegisterFragment : Fragment(), RegisterContract.View {
    private lateinit var presenter: RegisterContract.Presenter
    private lateinit var root: View

    private lateinit var editLoginId: EditText
    private lateinit var editPassword_1: EditText
    private lateinit var editPassword_2: EditText
    private lateinit var editEmail: EditText
    private lateinit var editFirstName: EditText
    private lateinit var editLastName: EditText
    private lateinit var signUpButton: ImageView
    private lateinit var loginButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        root = inflater.inflate(R.layout.new_fragment_resgister_api, container, false)

        setPresenter(RegisterPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        editLoginId = root.findViewById(R.id.editLoginId)
        editPassword_1 = root.findViewById(R.id.editPassword_1)
        editPassword_2 = root.findViewById(R.id.editPassword_2)
        editEmail = root.findViewById(R.id.editEmail)
        editFirstName = root.findViewById(R.id.editFirstName)
        editLastName = root.findViewById(R.id.editLastName)
        signUpButton = root.findViewById(R.id.buttonSignUp)
        loginButton = root.findViewById(R.id.textLogin)

        signUpButton.setOnClickListener {
            if (editPassword_1.text.toString() != editPassword_2.text.toString()) {
                displayToast("Error : password are not identical")
                return@setOnClickListener
            }
            presenter.register(editLoginId.text.toString(),
                editPassword_1.text.toString(),
                editEmail.text.toString(),
                editFirstName.text.toString(),
                editLastName.text.toString())

            displayLoading()
        }

        loginButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, ConnectionFragment())?.commit()
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

    override fun setAccessToken(token : String) {
        MainActivity.accessTokenSleewell = token
        fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, ProfileFragment())?.commit()
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: RegisterContract.Presenter) {
        this.presenter = presenter
    }
}