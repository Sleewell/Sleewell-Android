package com.sleewell.sleewell.mvp.menu.account.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.account.contract.AccountContract
import com.sleewell.sleewell.mvp.menu.account.presenter.AccountPresenter

class AccountFragment : Fragment(), AccountContract.View  {
    private lateinit var presenter: AccountContract.Presenter
    private lateinit var root: View

    private lateinit var disconnectionButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_account, container, false)

        setPresenter(AccountPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        if (MainActivity.accessTokenSleewell.isEmpty())
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, ConnectionFragment())?.commit()

        disconnectionButton = root.findViewById(R.id.disconnectionButton)
        disconnectionButton.setOnClickListener {
            MainActivity.accessTokenSleewell = ""
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, ConnectionFragment())?.commit()
        }
        return root
    }


    override fun setPresenter(presenter: AccountContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
