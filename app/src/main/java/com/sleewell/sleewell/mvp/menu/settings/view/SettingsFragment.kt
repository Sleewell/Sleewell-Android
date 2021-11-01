package com.sleewell.sleewell.mvp.menu.settings.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sleewell.sleewell.BuildConfig
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.settings.SettingsContract
import com.sleewell.sleewell.mvp.menu.settings.presenter.SettingsPresenter

private const val TITLE_TAG = "Settings"

class SettingsFragment : Fragment(), SettingsContract.View, PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    //Context
    private lateinit var presenter: SettingsContract.Presenter
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
                .replace(R.id.settings, SettingsFragment())
                .commit()
        } else {
            requireActivity().title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setPresenter(SettingsPresenter(this))

        return root
    }

    override fun setPresenter(presenter: SettingsContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

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
            setPreferencesFromResource(R.xml.settings_preferences, rootKey)
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

    class HelpPreferencesFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.help, rootKey)
            val versionCode = BuildConfig.VERSION_CODE
            val versionName = BuildConfig.VERSION_NAME

            val returnPref = findPreference<Preference>(getString(R.string.setting_help_return_key))
            returnPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                activity!!.supportFragmentManager.popBackStackImmediate()
                true
            }

            val signalPref = findPreference<Preference>(getString(R.string.setting_help_signal_problem_key))
            val urlReport = getString(R.string.url_report)
            signalPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("$urlReport?version=$versionName-$versionCode")
                    )
                )
                true
            }
        }
    }
}