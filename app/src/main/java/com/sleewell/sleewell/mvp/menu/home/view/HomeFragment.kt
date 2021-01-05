package com.sleewell.sleewell.mvp.menu.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.home.HomeContract
import com.sleewell.sleewell.mvp.menu.home.presenter.HomePresenter

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), HomeContract.View {
    //Context
    private lateinit var presenter: HomeContract.Presenter
    private lateinit var root: View

    //View widgets
    private lateinit var btnNfc: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        //get widgets
        this.btnNfc = root.findViewById(R.id.btn_nfc)

        //init event listeners
        btnNfc.setOnClickListener {
            navController.navigate(R.id.action_menuFragment_to_protocolFragment)
        }
    }

    /**
     * Display the nfc button on the screen
     *
     * @param state true - display, false - hide
     * @author Hugo Berthomé
     */
    override fun displayNfcButton(state: Boolean) {
        btnNfc.isEnabled = state
        if (state)
            btnNfc.visibility = View.VISIBLE
        else
            btnNfc.visibility = View.GONE
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