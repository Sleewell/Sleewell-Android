package com.sleewell.sleewell.new_mvp.menu.settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.settings.view.SettingsActivity
import com.sleewell.sleewell.new_mvp.menu.settings.SettingsContract
import com.sleewell.sleewell.new_mvp.menu.settings.presenter.SettingsPresenter

private const val TITLE_TAG = "Settings"

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment(), SettingsContract.View, PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    //Context
    private lateinit var presenter: SettingsContract.Presenter
    private lateinit var root: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.new_fragment_settings, container, false)
        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        } else {
            requireActivity().title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initActivityWidgets()
        setPresenter(SettingsPresenter(this))

        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.nav_menu) as? NavHostFragment
        val navController = nestedNavHostFragment?.navController

    }

    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Gabin Warnier de wailly
     */
    override fun setPresenter(presenter: SettingsContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    /**
     * Function called when quitting the activity
     *
     * @author Gabin Warnier de wailly
     */
    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    /**
     * Function called at the selection of a category of setting on the view
     * Will change the fragment
     *
     * @param caller
     * @param pref
     * @return boolean
     * @author Hugo Berthomé
     */
    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = activity!!.supportFragmentManager.fragmentFactory.instantiate(
            activity!!.classLoader,
            pref.fragment)
        fragment.arguments = args
        fragment.setTargetFragment(caller, 0)
        // Replace the existing Fragment with the new Fragment
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.settings, fragment)
            .addToBackStack(null)
            .commit()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, activity!!.title)
    }

    /*override fun onSupportNavigateUp(): Boolean {
        if (activity!!.supportFragmentManager.popBackStackImmediate()) {
            return true
        }
        return super.onSupportNavigateUp()
    }*/

    /**
     * Set the save of the user settings
     *
     * @author Gabin warnier de wailly
     */
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.new_settings, rootKey)
        }
    }

    /**
     * Class instantiate to display Networks settings
     *
     * @author Hugo Berthomé
     */
    class NetworkPreferencesFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.network, rootKey)
        }
    }

    /**
     * Class instantiate to display Notifications settings
     *
     * @author Hugo Berthomé
     */
    class NotificationPreferencesFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.notification, rootKey)
        }
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * It's will exit the settings activity's
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     *         proceed, true to consume it here.
     *
     * @author Gabin warnier de wailly
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        /*if (id == android.R.id.home) {
            finish()
        }*/
        return super.onOptionsItemSelected(item)
    }
}