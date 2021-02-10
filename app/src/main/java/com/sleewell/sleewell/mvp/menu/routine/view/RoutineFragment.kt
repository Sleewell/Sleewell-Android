package com.sleewell.sleewell.mvp.menu.routine.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.routine.Routine
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract
import com.sleewell.sleewell.mvp.menu.routine.presenter.RoutinePresenter

class RoutineFragment : Fragment(), RoutineContract.View {

    private lateinit var presenter: RoutineContract.Presenter
    private lateinit var root: View

    private lateinit var textDuration: TextView
    private lateinit var textMusicPlayer: TextView
    private lateinit var imageViewColorHalo: ImageView

    private lateinit var switchMusic: Switch
    private lateinit var switchHalo: Switch

    private lateinit var redBar: SeekBar
    private lateinit var greenBar: SeekBar
    private lateinit var blueBar: SeekBar
    
    private lateinit var routine: Routine

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.new_fragment_routine, container, false)

        setPresenter(RoutinePresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()


        routine =  Routine(100, 100, 100, true, 12, true, Routine.musicPlayer.MUSIC)

        initListView()

        return root
    }

    private fun initListView() {
        textDuration = root.findViewById(R.id.duration)
        textMusicPlayer = root.findViewById(R.id.musicPlayer)
        imageViewColorHalo = root.findViewById(R.id.resultColor)

        switchHalo = root.findViewById(R.id.switch_halo)
        switchMusic = root.findViewById(R.id.switch_music)

        redBar = root.findViewById(R.id.redBar)
        greenBar = root.findViewById(R.id.greenBar)
        blueBar = root.findViewById(R.id.blueBar)

        switchMusic.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                routine.setUseMusic(true)
                textMusicPlayer.text = "Music: " + routine.getMusicPlayer().name
            } else {
                routine.setUseMusic(false)
                textMusicPlayer.text = "Music: Not use"
            }
        }

        switchHalo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                routine.setUseHalo(true)
                textDuration.text = "Duration: " + routine.getDuration().toString()
            } else {
                routine.setUseHalo(false)
                textDuration.text = "Duration: Not use"
            }
        }


        redBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                routine.setColorRed(i)
                setColor()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        greenBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                routine.setColorGreen(i)
                setColor()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        blueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                routine.setColorBlue(i)
                setColor()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        setColor()
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: RoutineContract.Presenter) {
        this.presenter = presenter
    }

    private fun setColor() {
        imageViewColorHalo.setBackgroundColor(
            Color.rgb(
                routine.getColorRed(),
                routine.getColorGreen(),
                routine.getColorBlue()
            )
        )
    }
}