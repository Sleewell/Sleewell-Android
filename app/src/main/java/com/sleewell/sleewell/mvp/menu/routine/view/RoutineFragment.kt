package com.sleewell.sleewell.mvp.menu.routine.view

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.spotify.view.SpotifyFragment
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract
import com.sleewell.sleewell.mvp.menu.routine.RoutineListAdapter
import com.sleewell.sleewell.mvp.menu.routine.presenter.RoutinePresenter
import com.sleewell.sleewell.mvp.music.view.MusicFragment

class RoutineFragment : RoutineContract.View, Fragment(),  SpotifyFragment.OnInputSelected, MusicFragment.OnInputSelected {

    private lateinit var presenter: RoutineContract.Presenter
    private lateinit var root: View

    private lateinit var btn: ImageButton
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.new_fragment_routine, container, false)

        setPresenter(RoutinePresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        initListView()
        initSettingButton()

        return root
    }

    override fun sendInput(musicName: String, musicUri : String, musicImage : String,tag : String?) {
        presenter.updateSpotifyMusicSelected(musicName, musicUri, musicImage, tag)
    }

    override fun sendInput(musicName: String, tag: String?) {
        presenter.updateSleewellMusicSelected(musicName, tag)
    }

    private fun initListView() {
        listView = root.findViewById(R.id.routineListView)
        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, i, _ ->
            presenter.openRoutineDialog(i, fragmentManager, this)
        }

        btn = root.findViewById(R.id.button)
        btn.setOnClickListener {
            presenter.createNewItemRoutine(fragmentManager, this)
        }

        if (MainActivity.accessTokenSleewell.isNotEmpty())
            presenter.updateAdapter()
        else
            btn.visibility = View.INVISIBLE
    }

    private fun initSettingButton() {
        val buttonSettings = root.findViewById<ImageButton>(R.id.buttonSettings)
        buttonSettings.setOnClickListener {

            val navController = Navigation.findNavController(requireActivity(), R.id.nav_menu)
            navController.navigate(R.id.routineSettingsFragment)
        }
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: RoutineContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayRoutineList(routineAdapter: RoutineListAdapter) {
        listView.adapter = routineAdapter
    }

    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }
}