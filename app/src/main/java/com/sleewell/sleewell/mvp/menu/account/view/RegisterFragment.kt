package com.sleewell.sleewell.mvp.menu.account.view

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                Log.d("dfdf", editPassword_1.text.toString() + " " + editPassword_2.text.toString())
                return@setOnClickListener
            }
            presenter.register(editLoginId.text.toString(),
                editPassword_1.text.toString(),
                editEmail.text.toString(),
                editFirstName.text.toString(),
                editLastName.text.toString())
            displayToast("Register...")
        }

        loginButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, ConnectionFragment())?.commit()
        }
        return root
    }

    override fun setAccessToken(token : String) {
        MainActivity.accessTokenSleewell = token
        fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, AccountFragment())?.commit()
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: RegisterContract.Presenter) {
        this.presenter = presenter
    }
}