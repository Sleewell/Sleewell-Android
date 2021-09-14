package com.sleewell.sleewell.mvp.menu.home.view

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.home.HomeContract
import com.sleewell.sleewell.mvp.menu.home.presenter.HomePresenter
import com.sleewell.sleewell.mvp.protocol.view.ProtocolContainer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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
        val textClock = root.findViewById<TextClock>(R.id.clock)
        textClock.typeface = ResourcesCompat.getFont(this.activity as AppCompatActivity, R.font.nunito_light)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_main)

        this.dateView = root.findViewById(R.id.date)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateView.text = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEE. d MMM.", Locale.ENGLISH))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("EEE. d MMM.", Locale.ENGLISH)
            dateView.text = dateFormat.format(calendar.time)
        } else {
            val dateFormat = java.text.SimpleDateFormat("EEE. d MMM.", Locale.ENGLISH)
            dateView.text = dateFormat.format(Date())
        }

        val buttonProtocol = root.findViewById<Button>(R.id.button_protocol)
        buttonProtocol.setOnClickListener {
            val protocol = Intent(context, ProtocolContainer::class.java)
            startActivity(protocol)
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