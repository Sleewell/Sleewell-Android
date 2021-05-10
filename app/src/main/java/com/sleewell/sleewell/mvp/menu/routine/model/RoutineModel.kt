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
        val rt = Routine("", false,34, 23, 163)
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

    override fun updateSelectedItemRoutine(routine: Routine, nbr: Int) {
        for (i in aList.indices) {
            if (aList[i].isSelected) {
                aList[i].isSelected = false
                db.updateRoutine(aList[i])
            }
        }
        routine.isSelected = true
        updateItemRoutine(routine, nbr)
    }

    override fun updateSpotifyMusicSelected(musicName: String, musicUri: String, tag: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (musicName.isEmpty())
                return@launch
            val routine = tag?.toLong()?.let { db.getRoutine(it) }
            val nbr = aList.indexOf(routine)
            routine?.musicName = musicName
            routine?.musicUri = musicUri
            routine?.let { updateItemRoutine(it, nbr) }
            CoroutineScope(Dispatchers.Main).launch {
                if (dialog.isShowing) {
                    val nameMusicSelected = dialog.findViewById(R.id.musicNameSelectedDialog) as TextView
                    nameMusicSelected.text = routine?.musicName
                }
            }
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
            routine?.let { updateItemRoutine(it, nbr) }
            CoroutineScope(Dispatchers.Main).launch {
                if (dialog.isShowing) {
                    val nameMusicSelected = dialog.findViewById(R.id.musicNameSelectedDialog) as TextView
                    nameMusicSelected.text = routine?.musicName?.split("_")?.last()
                }
            }
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

    override fun getAdapter() : RoutineListAdapter {
        return adapter
    }

    override fun openRoutineParameter(nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment) {

        val routine = aList[nbr]

        dialog = openRoutineDialog(routine, nbr, fragmentManager, fragment)

        dialog.show()
    }

    @SuppressLint("ResourceType")
    private fun openRoutineDialog(routine: Routine, nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment): Dialog {
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
                removeNewItemRoutine(routine)
            }
            dialog.dismiss()
        }
        buttonSelect.setOnClickListener {
            routine.isSelected = true
            CoroutineScope(Dispatchers.IO).launch {
                updateSelectedItemRoutine(routine, nbr)
            }
            dialog.dismiss()
        }

        if (routine.name.isEmpty()) {
            title.setText("Routine ${routine.uId}")
        } else {
            title.setText(routine.name)
        }
        title.doAfterTextChanged {
            routine.name = title.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(routine, nbr)
            }
        }

        setDialogColorSet(dialog, routine, nbr)
        setDialogMusic(dialog, routine, nbr, fragmentManager, fragment)
        setDialogHalo(dialog, routine, nbr)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    private fun setDialogColorSet(dialog: Dialog, routine: Routine, nbr: Int) {

        val colorButtonBlue = dialog.findViewById(R.id.dialog_routine_halo_color_1) as Button
        val colorButtonWhite = dialog.findViewById(R.id.dialog_routine_halo_color_2) as Button
        val colorButtonRed = dialog.findViewById(R.id.dialog_routine_halo_color_3) as Button

        colorButtonBlue.setOnClickListener {
            val blueColor = ResourcesCompat.getColor(context.resources, R.color.haloColorBlue, null)
            routine.colorBlue = blueColor.blue
            routine.colorGreen = blueColor.green
            routine.colorRed = blueColor.red
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(routine, nbr)
            }
        }

        colorButtonWhite.setOnClickListener {
            val whiteColor = ResourcesCompat.getColor(context.resources, R.color.haloColorWhite, null)
            routine.colorBlue = whiteColor.blue
            routine.colorGreen = whiteColor.green
            routine.colorRed = whiteColor.red
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(routine, nbr)
            }
        }

        colorButtonRed.setOnClickListener {
            val redColor = ResourcesCompat.getColor(context.resources, R.color.haloColorRed, null)
            routine.colorBlue = redColor.blue
            routine.colorGreen = redColor.green
            routine.colorRed = redColor.red
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(routine, nbr)
            }
        }
    }

    private fun setDialogMusic(dialog: Dialog, routine: Routine, nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment) {

        val musicSwitch = dialog.findViewById(R.id.dialog_routine_music_switch) as SwitchCompat
        val musicIcon = dialog.findViewById(R.id.dialog_routine_music_icon) as ImageView
        val musicTitle = dialog.findViewById(R.id.dialog_routine_music_title) as TextView
        val playerMusicNameSpinner = dialog.findViewById(R.id.playerMusicNameSpinner) as Spinner
        val selectMusic = dialog.findViewById(R.id.dialog_routine_selectMusic_button) as ImageView
        val nameMusicSelected = dialog.findViewById(R.id.musicNameSelectedDialog) as TextView

        musicSwitch.isChecked = routine.useMusic
        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            routine.useMusic = isChecked
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(routine, nbr)
            }
            setDialogMusic(dialog, routine, nbr, fragmentManager, fragment)
        }
        selectMusic.setOnClickListener {
            if (routine.player == "Spotify") {
                val spotifyDialog = SpotifyFragment()
                spotifyDialog.setTargetFragment(fragment, 1)
                fragmentManager?.let { it -> spotifyDialog.show(it, routine.uId.toString()) }
            }
            if (routine.player == "Sleewell") {
                val musicDialog = MusicFragment()
                musicDialog.setTargetFragment(fragment, 1)
                fragmentManager?.let { it -> musicDialog.show(it, routine.uId.toString()) }
            }
        }

        ArrayAdapter.createFromResource(context, R.array.music_player, R.layout.spinner_text_item).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_text_item)
            playerMusicNameSpinner.adapter = adapter
            playerMusicNameSpinner.setSelection(
                context.resources.getStringArray(R.array.music_player).indexOf(routine.player)
            )
        }

        playerMusicNameSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                routine.player = playerMusicNameSpinner.selectedItem.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    updateItemRoutine(routine, nbr)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        if (routine.musicName.isNotEmpty()) {
            if (routine.player == "Sleewell")
                nameMusicSelected.text = routine.musicName.split("_").last()
            else
                nameMusicSelected.text = routine.musicName
        } else {
            nameMusicSelected.text = "None"
        }
        if (routine.useMusic) {
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

    private fun setDialogHalo(dialog: Dialog, routine: Routine, nbr: Int) {

        val haloTitle = dialog.findViewById(R.id.dialog_routine_halo_title) as TextView
        val haloSwitch = dialog.findViewById(R.id.dialog_routine_halo_switch) as SwitchCompat

        haloSwitch.isChecked = routine.useHalo
        haloSwitch.setOnCheckedChangeListener { _, isChecked ->
            routine.useHalo = isChecked
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(routine, nbr)
            }
            setDialogHalo(dialog, routine, nbr)
        }

        if (routine.useHalo) {
            haloTitle.text = "Turn on the halo"
            haloTitle.visibility = View.VISIBLE
        } else {
            haloTitle.visibility = View.INVISIBLE
        }
    }
}