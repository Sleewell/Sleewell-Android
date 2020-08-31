package com.sleewell.sleewell.mvp.settings.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.settings.SettingsContract
import androidx.preference.PreferenceFragmentCompat
import com.sleewell.sleewell.mvp.settings.presenter.SettingsPresenter

class SettingsActivity : AppCompatActivity(), SettingsContract.View {

    private lateinit var presenter: SettingsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setPresenter(SettingsPresenter(this))
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

        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}