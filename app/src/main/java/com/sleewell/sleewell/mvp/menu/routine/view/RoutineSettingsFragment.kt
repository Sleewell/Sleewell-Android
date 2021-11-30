package com.sleewell.sleewell.mvp.menu.routine.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sleewell.sleewell.R

private const val TITLE_TAG = "Settings"

class RoutineSettingsFragment : Fragment(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    //Context
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.new_fragment_settings, container, false)
        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, RoutineSettingsFragment())
                .commit()
        } else {
            requireActivity().title = savedInstanceState.getCharSequence(TITLE_TAG)
        }

        return root
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

    /**
     * Set the save of the user settings
     *
     * @author Titouan FIANCETTE
     */
    class RoutineSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings_routine, rootKey)
            val returnPref = findPreference<Preference>(getString(R.string.setting_routine_return_key))
            returnPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                this.childFragmentManager.popBackStack()
                true
            }
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
            val returnPref = findPreference<Preference>(getString(R.string.setting_network_return_key))
            returnPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                true
            }
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
            val returnPref = findPreference<Preference>(getString(R.string.setting_notification_return_key))
            returnPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                true
            }
        }
    }
}