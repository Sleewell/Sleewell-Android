package com.sleewell.sleewell.mvp.music.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.music.MainContract
import es.claucookie.miniequalizerlibrary.EqualizerView

private lateinit var root: View

class MusicSelectFragment : Fragment(), MainContract.View {

    private lateinit var listView: ListView
    private lateinit var presenter: MainContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_music_select, container, false)

        //setPresenter(MusicSelectPresenter(this, this.activity as AppCompatActivity))
        //this.initListView()
        //this.initActivityWidgets()
        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        val circle = root.findViewById<View>(R.id.circle_forest)

        val equalizer = root.findViewById<View>(R.id.equalizer_view) as EqualizerView

        val buttonMusic = root.findViewById<ImageButton>(R.id.editMusicId)
        buttonMusic.setOnClickListener {
            circle.visibility = View.VISIBLE
            //circle2.visibility = View.INVISIBLE
            if (equalizer.isAnimating) {
                equalizer.visibility = View.INVISIBLE
                equalizer.stopBars() // When you want equalizer stops animating
            } else {
                equalizer.animateBars() // Whenever you want to tart the animation
                equalizer.visibility = View.VISIBLE
                //equalizer2.visibility = View.INVISIBLE
                //equalizer2.stopBars()
            }
        }
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }
}