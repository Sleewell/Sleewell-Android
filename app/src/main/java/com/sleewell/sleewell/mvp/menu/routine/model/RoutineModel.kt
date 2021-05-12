package com.sleewell.sleewell.mvp.menu.routine.model

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sleewell.sleewell.R
import com.sleewell.sleewell.Spotify.View.SpotifyFragment
import com.sleewell.sleewell.database.routine.RoutineDao
import com.sleewell.sleewell.database.routine.RoutineDatabase
import com.sleewell.sleewell.database.routine.entities.Routine
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract
import com.sleewell.sleewell.mvp.menu.routine.RoutineListAdapter
import com.sleewell.sleewell.mvp.music.view.MusicFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RoutineModel(context: Context) : RoutineContract.Model {

    private var context = context
    private var db: RoutineDao = RoutineDatabase.getDatabase(context).routineDao()
    private var adapter: RoutineListAdapter
    private var aList: ArrayList<Routine> = ArrayList()
    private lateinit var dialog: Dialog

    init {
        aList.clear()
        adapter = RoutineListAdapter(context, aList)
    }

    override fun createNewItemRoutine() {
        val rt = Routine("", false, 34, 23, 163)
        val n = db.addNewRoutine(rt)

        aList.add(db.getRoutine(n))
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    override fun removeNewItemRoutine(routine: Routine) {
        db.deleteRoutine(routine)
        aList.remove(routine)
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    override fun updateItemRoutine(routine: Routine, nbr: Int) {
        db.updateRoutine(routine)
        aList[nbr] = routine
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    override fun updateSelectedItemRoutine(nbr: Int) {
        for (i in aList.indices) {
            if (aList[i].isSelected) {
                aList[i].isSelected = false
                db.updateRoutine(aList[i])
            }
        }
        aList[nbr].isSelected = true
        updateItemRoutine(aList[nbr], nbr)
    }

    override fun updateSpotifyMusicSelected(musicName: String, musicUri: String, tag: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (musicName.isEmpty())
                return@launch
            val routine = tag?.toLong()?.let { db.getRoutine(it) }
            val nbr = aList.indexOf(routine)
            routine?.musicName = musicName
            routine?.musicUri = musicUri
            CoroutineScope(Dispatchers.Main).launch {
                if (dialog.isShowing) {
                    val nameMusicSelected =
                        dialog.findViewById(R.id.musicNameSelectedDialog) as TextView
                    nameMusicSelected.text = routine?.musicName
                }
            }
            routine?.let { updateItemRoutine(it, nbr) }
        }
    }

    override fun updateSleewellMusicSelected(musicName: String, tag: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (musicName.isEmpty())
                return@launch
            val routine = tag?.toLong()?.let { db.getRoutine(it) }
            val nbr = aList.indexOf(routine)
            routine?.musicName = musicName
            routine?.musicUri = ""
            CoroutineScope(Dispatchers.Main).launch {
                if (dialog.isShowing) {
                    val nameMusicSelected =
                        dialog.findViewById(R.id.musicNameSelectedDialog) as TextView
                    nameMusicSelected.text = routine?.musicName?.split("_")?.last()
                }
            }
            routine?.let { updateItemRoutine(it, nbr) }
        }
    }

    override fun updateListViewRoutine() {

        val routines = db.getAllRoutine()

        aList.clear()
        for (i in routines.indices) {
            aList.add(routines[i])
        }
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    override fun getAdapter(): RoutineListAdapter {
        return adapter
    }

    override fun openRoutineParameter(
        nbr: Int,
        fragmentManager: FragmentManager?,
        fragment: Fragment
    ) {
        dialog = openRoutineDialog(nbr, fragmentManager, fragment)
        dialog.show()
    }

    @SuppressLint("ResourceType")
    private fun openRoutineDialog(
        nbr: Int,
        fragmentManager: FragmentManager?,
        fragment: Fragment
    ): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.routine_layout)

        val buttonDelete = dialog.findViewById(R.id.dialog_routine_button_delete) as ImageButton
        val buttonSelect = dialog.findViewById(R.id.dialog_routine_selected_button) as Button
        val buttonClose = dialog.findViewById(R.id.dialog_routine_close) as Button
        val title = dialog.findViewById(R.id.dialog_routine_title) as EditText

        buttonClose.setOnClickListener { dialog.dismiss() }
        buttonDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                removeNewItemRoutine(aList[nbr])
            }
            dialog.dismiss()
        }
        buttonSelect.setOnClickListener {
            aList[nbr].isSelected = true
            CoroutineScope(Dispatchers.IO).launch {
                updateSelectedItemRoutine(nbr)
            }
            dialog.dismiss()
        }

        if (aList[nbr].name.isEmpty()) {
            title.setText("Routine ${aList[nbr].uId}")
        } else {
            title.setText(aList[nbr].name)
        }
        title.doAfterTextChanged {
            aList[nbr].name = title.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(aList[nbr], nbr)
            }
        }

        setDialogColorSet(dialog, nbr)
        setDialogMusic(dialog, nbr, fragmentManager, fragment)
        setDialogHalo(dialog, nbr)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    private fun setDialogColorSet(dialog: Dialog, nbr: Int) {
        val listBtn = arrayOf(
            R.id.dialog_routine_halo_color_1,
            R.id.dialog_routine_halo_color_2,
            R.id.dialog_routine_halo_color_3,
            R.id.dialog_routine_halo_color_4,
            R.id.dialog_routine_halo_color_5,
            R.id.dialog_routine_halo_color_6
        )

        listBtn.forEach {
            val btn = dialog.findViewById(it) as Button

            btn.setOnClickListener {
                val btnColor = (btn.background as ColorDrawable).color
                val routine = aList[nbr]
                routine.colorBlue = btnColor.blue
                routine.colorGreen = btnColor.green
                routine.colorRed = btnColor.red
                CoroutineScope(Dispatchers.IO).launch {
                    updateItemRoutine(routine, nbr)
                }
            }
        }
    }

    private fun setDialogMusic(
        dialog: Dialog,
        nbr: Int,
        fragmentManager: FragmentManager?,
        fragment: Fragment
    ) {

        val musicSwitch = dialog.findViewById(R.id.dialog_routine_music_switch) as SwitchCompat
        val musicIcon = dialog.findViewById(R.id.dialog_routine_music_icon) as ImageView
        val musicTitle = dialog.findViewById(R.id.dialog_routine_music_title) as TextView
        val playerMusicNameSpinner = dialog.findViewById(R.id.playerMusicNameSpinner) as Spinner
        val selectMusic = dialog.findViewById(R.id.dialog_routine_selectMusic_button) as ImageView
        val nameMusicSelected = dialog.findViewById(R.id.musicNameSelectedDialog) as TextView

        musicSwitch.isChecked = aList[nbr].useMusic
        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            val routine = aList[nbr]
            routine.useMusic = isChecked
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(routine, nbr)
            }
            setDialogMusic(dialog, nbr, fragmentManager, fragment)
        }
        selectMusic.setOnClickListener {
            if (aList[nbr].player == "Spotify") {
                val spotifyDialog = SpotifyFragment()
                spotifyDialog.setTargetFragment(fragment, 1)
                fragmentManager?.let { it -> spotifyDialog.show(it, aList[nbr].uId.toString()) }
            }
            if (aList[nbr].player == "Sleewell") {
                val musicDialog = MusicFragment()
                musicDialog.setTargetFragment(fragment, 1)
                fragmentManager?.let { it -> musicDialog.show(it, aList[nbr].uId.toString()) }
            }
        }

        ArrayAdapter.createFromResource(context, R.array.music_player, R.layout.spinner_text_item)
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_text_item)
                playerMusicNameSpinner.adapter = adapter
                playerMusicNameSpinner.setSelection(
                    context.resources.getStringArray(R.array.music_player)
                        .indexOf(aList[nbr].player)
                )
            }

        playerMusicNameSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if (aList[nbr].player == playerMusicNameSpinner.selectedItem.toString())
                    return
                aList[nbr].player = playerMusicNameSpinner.selectedItem.toString()
                nameMusicSelected.text = "None"
                aList[nbr].musicName = "None"
                aList[nbr].musicUri = ""
                CoroutineScope(Dispatchers.IO).launch {
                    updateItemRoutine(aList[nbr], nbr)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        if (aList[nbr].musicName.isNotEmpty()) {
            if (aList[nbr].player == "Sleewell")
                nameMusicSelected.text = aList[nbr].musicName.split("_").last()
            else
                nameMusicSelected.text = aList[nbr].musicName
        } else {
            nameMusicSelected.text = "None"
        }
        if (aList[nbr].useMusic) {
            musicIcon.setBackgroundResource(R.drawable.ic_music_on_blue)
            musicTitle.visibility = View.VISIBLE
            musicTitle.text = "Turn on the music"
            playerMusicNameSpinner.visibility = View.VISIBLE
            selectMusic.visibility = View.VISIBLE
            nameMusicSelected.visibility = View.VISIBLE
        } else {
            musicIcon.setBackgroundResource(R.drawable.ic_music_off_blue)
            musicTitle.visibility = View.INVISIBLE
            playerMusicNameSpinner.visibility = View.GONE
            selectMusic.visibility = View.GONE
            nameMusicSelected.visibility = View.GONE
        }
    }

    private fun setDialogHalo(dialog: Dialog, nbr: Int) {

        val haloTitle = dialog.findViewById(R.id.dialog_routine_halo_title) as TextView
        val haloSwitch = dialog.findViewById(R.id.dialog_routine_halo_switch) as SwitchCompat

        haloSwitch.isChecked = aList[nbr].useHalo
        haloSwitch.setOnCheckedChangeListener { _, isChecked ->
            aList[nbr].useHalo = isChecked
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(aList[nbr], nbr)
            }
            setDialogHalo(dialog, nbr)
        }

        if (aList[nbr].useHalo) {
            haloTitle.text = "Turn on the halo"
            haloTitle.visibility = View.VISIBLE
        } else {
            haloTitle.visibility = View.INVISIBLE
        }
    }
}