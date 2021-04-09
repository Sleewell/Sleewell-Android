package com.sleewell.sleewell.mvp.menu.connection.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.Spotify.Presenter.SpotifyPresenter
import com.sleewell.sleewell.mvp.menu.connection.ConnectionContract
import com.sleewell.sleewell.mvp.menu.connection.presenter.ConnectionPresenter

class ConnectionFragment : Fragment(), ConnectionContract.View {

    private lateinit var presenter: ConnectionContract.Presenter
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_connection_api, container, false)

        setPresenter(ConnectionPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        return root
    }

    override fun setPresenter(presenter: ConnectionContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}