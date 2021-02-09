package com.sleewell.sleewell.mvp.menu.routine.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract
import com.sleewell.sleewell.mvp.menu.routine.presenter.RoutinePresenter

class RoutineFragment : Fragment(), RoutineContract.View {

    private lateinit var presenter: RoutineContract.Presenter
    private lateinit var root: View

    private lateinit var textDuration: TextView
    private lateinit var textMusicPlayer: TextView
    private lateinit var imageViewColorHalo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.new_fragment_routine, container, false)

        setPresenter(RoutinePresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        initListView()

        return root
    }

    private fun initListView() {
        textDuration = root.findViewById(R.id.duration)
        textMusicPlayer = root.findViewById(R.id.musicPlayer)
        imageViewColorHalo = root.findViewById(R.id.resultColor)
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: RoutineContract.Presenter) {
        this.presenter = presenter
    }
}