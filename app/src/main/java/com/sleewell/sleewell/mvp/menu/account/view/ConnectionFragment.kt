package com.sleewell.sleewell.mvp.menu.account.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.account.contract.ConnectionContract
import com.sleewell.sleewell.mvp.menu.account.presenter.ConnectionPresenter

class ConnectionFragment : Fragment(), ConnectionContract.View {

    private lateinit var presenter: ConnectionContract.Presenter
    private lateinit var root: View

    private lateinit var loginButton: ImageView
    private lateinit var signUpButton: TextView
    private lateinit var editName: EditText
    private lateinit var editPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        root = inflater.inflate(R.layout.new_fragment_connection_api, container, false)

        setPresenter(ConnectionPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        loginButton = root.findViewById(R.id.loginImageLogin)
        signUpButton = root.findViewById(R.id.loginTextSignup)
        editName = root.findViewById(R.id.editTextName)
        editPassword = root.findViewById(R.id.editTextPassword)
        loginButton.setOnClickListener {
            presenter.login(editName.text.toString(), editPassword.text.toString())
            displayToast("Login...")
        }
        signUpButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, RegisterFragment())?.commit()
        }
        return root
    }

    override fun setAccessToken(token : String) {
        MainActivity.accessTokenSleewell = token
        fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, AccountFragment())?.commit()
    }

    override fun setPresenter(presenter: ConnectionContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}