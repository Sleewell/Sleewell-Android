package com.sleewell.sleewell.mvp.menu.home.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.home.HomeContract
import com.sleewell.sleewell.mvp.menu.home.presenter.HomePresenter
import com.sleewell.sleewell.mvp.protocol.view.ProtocolContainer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(), HomeContract.View {
    //Context
    private lateinit var presenter: HomeContract.Presenter
    private lateinit var root: View

    //View widgets
    private lateinit var dateView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.new_fragment_home, container, false)
        initActivityWidgets()
        setPresenter(HomePresenter(this, this.activity as AppCompatActivity))

        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.dateView = root.findViewById(R.id.date)
            dateView.text = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE d LLL"))
        }

        val buttonProtocol = root.findViewById<Button>(R.id.button_protocol)
        buttonProtocol.setOnClickListener {
            val protocol = Intent(context, ProtocolContainer::class.java)
            startActivity(protocol)
        }

        val buttonMusic = root.findViewById<Button>(R.id.button_music)
        buttonMusic.setOnClickListener {
            navController.navigate(R.id.action_menuFragment_to_musicFragment)
        }
    }

    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Hugo Berthomé
     */
    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    /**
     * Function called when the screen reappeared on this activity when quiting temporally the app
     */
    override fun onResume() {
        super.onResume()
        presenter.onViewCreated()
    }

    /**
     * Function called when quitting the activity
     */
    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }
}