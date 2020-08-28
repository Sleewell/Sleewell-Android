package com.sleewell.sleewell.mvp.main.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.main.MainContract
import com.sleewell.sleewell.mvp.main.presenter.MainPresenter
import com.sleewell.sleewell.mvp.settings.SettingsContract
import com.sleewell.sleewell.mvp.settings.presenter.SettingsPresenter
import com.sleewell.sleewell.mvp.settings.view.SettingsActivity

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_alarms,
                R.id.navigation_music,
                R.id.navigation_statistics
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setPresenter(MainPresenter(this, this))
    }

    /**
     * Function called when quitting the activity
     *
     * @author Hugo Berthomé
     */
    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Hugo Berthomé
     */
    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * Here is for the settings
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @author Gabin warnier de wailly
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * It's will launch the settings activity's
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

        if (id == R.id.settings_action) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
