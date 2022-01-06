package com.sleewell.sleewell.mvp.menu.routine.model

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.sleewell.ApiClient
import com.sleewell.sleewell.api.sleewell.IRoutineApi
import com.sleewell.sleewell.api.sleewell.model.AddRoutineResponse
import com.sleewell.sleewell.api.sleewell.model.DeleteRoutineResponse
import com.sleewell.sleewell.api.sleewell.model.RoutinesResponse
import com.sleewell.sleewell.api.sleewell.model.UpdateRoutineResponse
import com.sleewell.sleewell.database.routine.RoutineDao
import com.sleewell.sleewell.database.routine.RoutineDatabase
import com.sleewell.sleewell.database.routine.entities.Routine
import com.sleewell.sleewell.database.routine.entities.RoutineState
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract
import com.sleewell.sleewell.mvp.menu.routine.RoutineListAdapter
import com.sleewell.sleewell.mvp.music.view.MusicFragment
import com.sleewell.sleewell.mvp.spotify.view.SpotifyFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback


class RoutineModel(private var context: Context) : RoutineContract.Model {

    private var db: RoutineDao = RoutineDatabase.getDatabase(context).routineDao()
    private var adapter: RoutineListAdapter
    private var aList: ArrayList<Routine> = ArrayList()
    private lateinit var dialog: Dialog
    private var api : IRoutineApi? = ApiClient.retrofit.create(IRoutineApi::class.java)

    init {
        aList.clear()
        adapter = RoutineListAdapter(context, aList)
    }

    override fun createNewItemRoutine(fragmentManager: FragmentManager?, fragment: Fragment) {
        val rt = Routine(
            "",
            false,
            -1,
            48,
            63,
            159,
            false,
            48,
            false,
            "None",
            "",
            "",
            "",
            RoutineState.NEW.ordinal
        )
        val n = db.addNewRoutine(rt)

        aList.add(db.getRoutine(n))
        addRoutineApiSleewell(db.getRoutine(n))
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
            val nbr = aList.size - 1
            val dialogNew = openRoutineDialog(nbr, fragmentManager, fragment)
            val title = dialogNew.findViewById(R.id.dialog_routine_title) as EditText

            dialogNew.setOnDismissListener {
                if (aList.isEmpty())
                    return@setOnDismissListener
                when(aList[nbr].state) {
                    RoutineState.DELETE.ordinal -> {
                        deleteRoutineApiSleewell(aList[nbr])
                        aList.removeAt(nbr)
                        adapter.notifyDataSetChanged()
                    }

                    RoutineState.UPDATE.ordinal -> {
                        updateRoutineApiSleewell(aList[nbr])
                    }
                }
            }
            dialogNew.show()
            title.onFocusChangeListener = OnFocusChangeListener { _, _ ->
                title.post {
                    val inputMethodManager: InputMethodManager = dialogNew.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(title, InputMethodManager.SHOW_IMPLICIT)
                }
            }
            title.requestFocus()
        }
    }

    override fun removeNewItemRoutine(routine: Routine) {
        aList.remove(routine)
        adapter.notifyDataSetChanged()
    }

    override fun updateItemRoutine(routine: Routine, nbr: Int) {
        if (routine.state != RoutineState.NEW.ordinal) {
            routine.state = RoutineState.UPDATE.ordinal
        }
        CoroutineScope(Dispatchers.IO).launch {
            db.updateRoutine(routine)
        }
        aList[nbr] = routine
        aList[nbr].state = RoutineState.UPDATE.ordinal
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

    override fun updateSpotifyMusicSelected(musicName: String, musicUri: String, musicImage: String, tag: String?) {
        if (musicName.isEmpty())
            return
        val routine = aList.find { it.uId == tag?.toLong() }
        val nbr = aList.indexOf(routine)
        routine?.musicName = musicName
        routine?.musicUri = musicUri
        routine?.imagePlaylist = musicImage
        if (dialog.isShowing) {
            val nameMusicSelected = dialog.findViewById(R.id.musicNameSelectedDialog) as TextView
            nameMusicSelected.text = routine?.musicName
        }
        Log.d("TEST", musicImage)
        routine?.let { updateItemRoutine(it, nbr) }
    }

    override fun updateSleewellMusicSelected(musicName: String, tag: String?) {
        if (musicName.isEmpty())
            return
        val routine = aList.find { it.uId == tag?.toLong() }
        val nbr = aList.indexOf(routine)
        routine?.musicName = musicName
        routine?.musicUri = ""
        routine?.imagePlaylist = ""
        if (::dialog.isInitialized && dialog.isShowing) {
            val nameMusicSelected = dialog.findViewById(R.id.musicNameSelectedDialog) as TextView
            nameMusicSelected.text = routine?.musicName?.split("_")?.last()
        }
        routine?.let { updateItemRoutine(it, nbr) }
    }

    private fun convertToRoutine(ApiRoutine: com.sleewell.sleewell.api.sleewell.model.Routine) : Routine {
        val id = ApiRoutine.id
        val initColor = Color.parseColor(ApiRoutine.color)
        val r = Color.red(initColor)
        val g = Color.green(initColor)
        val b = Color.blue(initColor)
        val halo = ApiRoutine.halo == 1
        val duration = ApiRoutine.duration
        val useMusic = ApiRoutine.usemusic == 1
        val musicName = ApiRoutine.musicName
        val musicUri = ApiRoutine.musicUri
        val player = ApiRoutine.player
        val name = ApiRoutine.name

        return Routine(name, false, id, r, g, b, halo, duration, useMusic, player, musicName, musicUri)
    }

    private fun updateListViewRoutine(routines: RoutinesResponse?) {
        val routinesInDB = db.getAllRoutine()

        for (i in routines?.data!!.indices) {
            val isRoutine = routinesInDB.find { it.apiId == routines.data[i].id }
            if (isRoutine != null) {
                if (isRoutine.state != RoutineState.UPDATE.ordinal) {
                    val rt = convertToRoutine(routines.data[i])
                    isRoutine.copy(
                        colorRed = rt.colorRed,
                        colorBlue = rt.colorBlue,
                        colorGreen = rt.colorGreen,
                        useHalo = rt.useHalo,
                        duration = rt.duration,
                        useMusic = rt.useMusic,
                        musicName = rt.musicName,
                        player = rt.player,
                        name = rt.name
                    )
                    db.updateRoutine(isRoutine)
                }
            } else {
                val rt = db.addNewRoutine(convertToRoutine(routines.data[i]))
                aList.add(db.getRoutine(rt))
            }
        }
        routinesInDB.forEach {
            val rt = it
            val isRoutine = routines.data.find { it.id == rt.apiId }

            if (isRoutine == null) {
                val indexRoutineDel = aList.indexOf(rt)
                if (indexRoutineDel != -1)
                    aList.removeAt(aList.indexOf(rt))
                db.deleteRoutine(rt)
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    override fun updateListViewOffLine() {
        val routines = db.getAllRoutine()

        aList.clear()
        for (i in routines.indices) {
            when (routines[i].state) {
                RoutineState.NEW.ordinal -> {
                    aList.add(routines[i])
                    addRoutineApiSleewell(routines[i])
                }
                RoutineState.DELETE.ordinal -> deleteRoutineApiSleewell(routines[i])
                RoutineState.UPDATE.ordinal -> {
                    aList.add(routines[i])
                    updateRoutineApiSleewell(routines[i])
                }
                RoutineState.NONE.ordinal -> aList.add(routines[i])
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    override fun getRoutineApiSleewell() {
        val call : Call<RoutinesResponse>? = api?.getRoutines(MainActivity.accessTokenSleewell)
        val tag = "GET-ROUTINE-API"

        CoroutineScope(Dispatchers.IO).launch {
            updateListViewOffLine()
        }
        call?.enqueue(object : Callback<RoutinesResponse> {
            override fun onResponse(
                call: Call<RoutinesResponse>,
                response: retrofit2.Response<RoutinesResponse>
            ) {
                val responseRes: RoutinesResponse? = response.body()

                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : $response")
                } else {
                    Log.e(tag, "Success")
                    CoroutineScope(Dispatchers.IO).launch {
                        updateListViewRoutine(responseRes)
                    }
                }
            }

            override fun onFailure(call: Call<RoutinesResponse>, t: Throwable) {
                Log.e(tag, t.toString())
            }
        })
    }

    private fun deleteRoutineApiSleewell(routine: Routine) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val tag = "DEL-ROUTINE-API"

        builder.addFormDataPart("id", routine.apiId.toString())

        val requestBody: RequestBody = builder.build()
        val call : Call<DeleteRoutineResponse>? = api?.deleteRoutine(
            MainActivity.accessTokenSleewell,
            requestBody
        )

        call?.enqueue(object : Callback<DeleteRoutineResponse> {

            override fun onResponse(
                call: Call<DeleteRoutineResponse>,
                response: retrofit2.Response<DeleteRoutineResponse>
            ) {
                val responseRes: DeleteRoutineResponse? = response.body()
                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                } else {
                    Log.e(tag, "Success")
                    CoroutineScope(Dispatchers.IO).launch {
                        db.deleteRoutine(routine)
                    }
                }
            }

            override fun onFailure(call: Call<DeleteRoutineResponse>, t: Throwable) {
                Log.e(tag, t.toString())
            }
        })
    }

    private fun updateRoutineApiSleewell(routine: Routine) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val tag = "UPDATE-ROUTINE-API"
        val hex = String.format(
            "#%02x%02x%02x",
            routine.colorRed,
            routine.colorGreen,
            routine.colorBlue
        )
        val useHalo = if (routine.useHalo) 1 else 0
        val useMusic = if (routine.useMusic) 1 else 0

        builder.addFormDataPart("name", routine.name)
        builder.addFormDataPart("color", hex)
        builder.addFormDataPart("music", useMusic.toString())
        builder.addFormDataPart("halo", useHalo.toString())
        builder.addFormDataPart("duration", routine.duration.toString())
        builder.addFormDataPart("player", routine.player)
        builder.addFormDataPart("musicname", routine.musicName)
        builder.addFormDataPart("musicuri", routine.musicUri)
        builder.addFormDataPart("id", routine.apiId.toString())

        val requestBody: RequestBody = builder.build()
        val call : Call<UpdateRoutineResponse>? = api?.updateRoutine(
            MainActivity.accessTokenSleewell,
            requestBody
        )

        call?.enqueue(object : Callback<UpdateRoutineResponse> {

            override fun onResponse(
                call: Call<UpdateRoutineResponse>,
                response: retrofit2.Response<UpdateRoutineResponse>
            ) {
                val responseRes: UpdateRoutineResponse? = response.body()
                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                } else {
                    Log.e(tag, "Success")
                    if (response.code() == 200) {
                        val index = aList.indexOf(routine)
                        if (index != -1) {
                            routine.state = RoutineState.NONE.ordinal
                            aList[index] = routine
                            adapter.notifyDataSetChanged()
                            CoroutineScope(Dispatchers.IO).launch {
                                db.updateRoutine(routine)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UpdateRoutineResponse>, t: Throwable) {
                Log.e(tag, t.toString())
            }
        })
    }

    private fun addRoutineApiSleewell(routine: Routine) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val tag = "ADD-ROUTINE-API"
        val hex = String.format(
            "#%02x%02x%02x",
            routine.colorRed,
            routine.colorGreen,
            routine.colorBlue
        )
        val useHalo = if (routine.useHalo) 1 else 0
        val useMusic = if (routine.useMusic) 1 else 0

        builder.addFormDataPart("name", routine.name)
        builder.addFormDataPart("color", hex)
        builder.addFormDataPart("music", useMusic.toString())
        builder.addFormDataPart("halo", useHalo.toString())
        builder.addFormDataPart("duration", routine.duration.toString())
        builder.addFormDataPart("player", routine.player)
        builder.addFormDataPart("musicname", routine.musicName)
        builder.addFormDataPart("musicuri", routine.musicUri)

        val requestBody: RequestBody = builder.build()
        val call : Call<AddRoutineResponse>? = api?.addRoutine(MainActivity.accessTokenSleewell, requestBody)

        call?.enqueue(object : Callback<AddRoutineResponse> {
            override fun onResponse(
                call: Call<AddRoutineResponse>,
                response: retrofit2.Response<AddRoutineResponse>
            ) {
                val responseRes: AddRoutineResponse? = response.body()
                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                } else {
                    Log.e(tag, "Success")
                    if (response.code() == 200) {
                        val index = aList.indexOf(routine)
                        routine.state = RoutineState.NONE.ordinal
                        routine.apiId = responseRes.id
                        if (index == -1)
                            aList.add(routine)
                        else
                            aList[index] = routine
                        adapter.notifyDataSetChanged()
                        CoroutineScope(Dispatchers.IO).launch {
                            db.updateRoutine(routine)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AddRoutineResponse>, t: Throwable) {
                Log.e(tag, t.toString())
            }
        })
    }

    fun saveRoutineFromList() {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in aList.indices) {
                if (aList[i].uId == 0.toLong()) {
                    val rt = Routine(aList[i].name, aList[i].isSelected, aList[i].apiId, aList[i].colorRed, aList[i].colorGreen, aList[i].colorBlue, aList[i].useHalo, aList[i].duration, aList[i].useMusic, aList[i].player, aList[i].musicName, aList[i].musicUri, aList[i].imagePlaylist, aList[i].state)
                    db.addNewRoutine(rt)
                } else {
                    db.updateRoutine(aList[i])
                }
            }
        }
    }

    override fun getAdapter() : RoutineListAdapter {
        return adapter
    }

    override fun openRoutineParameter(nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment) {
        dialog = openRoutineDialog(nbr, fragmentManager, fragment)
        dialog.setOnDismissListener {
            if (aList.isEmpty())
                return@setOnDismissListener
            when(aList[nbr].state) {
                RoutineState.DELETE.ordinal -> {
                    deleteRoutineApiSleewell(aList[nbr])
                    aList.removeAt(nbr)
                    adapter.notifyDataSetChanged()
                }

                RoutineState.UPDATE.ordinal -> {
                    updateRoutineApiSleewell(aList[nbr])
                }
            }
        }
        dialog.show()
    }

    @SuppressLint("ResourceType")
    private fun openRoutineDialog(nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.routine_layout)

        val buttonDelete = dialog.findViewById(R.id.dialog_routine_button_delete) as ImageButton
        val buttonSelect = dialog.findViewById(R.id.dialog_routine_selected_button) as TextView
        val buttonClose = dialog.findViewById(R.id.dialog_routine_close) as Button
        val title = dialog.findViewById(R.id.dialog_routine_title) as EditText

        buttonClose.setOnClickListener { dialog.dismiss() }
        buttonDelete.setOnClickListener {
            aList[nbr].state = RoutineState.DELETE.ordinal
            CoroutineScope(Dispatchers.IO).launch {
                db.updateRoutine(aList[nbr])
            }
            dialog.dismiss()
        }
        buttonSelect.setOnClickListener {
            for (item in aList) {
                if (item.isSelected && aList[nbr] != item) {
                    item.isSelected = false
                    updateItemRoutine(item, aList.indexOf(item))
                }
            }
            aList[nbr].isSelected = true
            updateItemRoutine(aList[nbr], nbr)
            dialog.dismiss()
        }

        if (aList[nbr].name.isEmpty()) {
            title.setText("Routine ${aList[nbr].apiId}")
        } else {
            title.setText(aList[nbr].name)
        }
        title.doAfterTextChanged {
            aList[nbr].name = title.text.toString()
            updateItemRoutine(aList[nbr], nbr)
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
            val btn = dialog.findViewById(it) as ImageView
            val iconColor = dialog.findViewById(R.id.dialog_routine_halo_color) as ImageView
            val iconHalo = dialog.findViewById(R.id.dialog_routine_halo_icon) as ImageView
            val btnColor = (btn.background as ColorDrawable).color
            val routine = aList[nbr]

            btn.setOnClickListener {
                routine.colorBlue = btnColor.blue
                routine.colorGreen = btnColor.green
                routine.colorRed = btnColor.red
                listBtn.forEach {
                    (dialog.findViewById(it) as ImageView).setImageResource(0)
                }
                btn.setImageResource(R.drawable.ic_check_blue)
                CoroutineScope(Dispatchers.IO).launch {
                    updateItemRoutine(routine, nbr)
                }
            }
            if (Color.rgb(routine.colorRed, routine.colorGreen, routine.colorBlue) == Color.rgb(btnColor.red, btnColor.green, btnColor.blue)) {
                btn.setImageResource(R.drawable.ic_check_blue)
            }

            if (aList[nbr].useHalo) {
                iconHalo.setBackgroundResource(R.drawable.ic_halo_on_blue)
                iconColor.visibility = View.VISIBLE
                btn.visibility = View.VISIBLE
            } else {
                iconHalo.setBackgroundResource(R.drawable.ic_halo_off_blue)
                iconColor.visibility = View.INVISIBLE
                btn.visibility = View.INVISIBLE
            }
        }
    }

    private fun setDialogMusic(dialog: Dialog, nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment) {

        val musicSwitch = dialog.findViewById(R.id.dialog_routine_music_switch) as SwitchCompat
        val musicIcon = dialog.findViewById(R.id.dialog_routine_music_icon) as ImageView
        val playerMusicNameSpinner = dialog.findViewById(R.id.playerMusicNameSpinner) as Spinner
        val selectMusic = dialog.findViewById(R.id.dialog_routine_selectMusic_button) as ImageView
        val nameMusicSelected = dialog.findViewById(R.id.musicNameSelectedDialog) as TextView

        musicSwitch.isChecked = aList[nbr].useMusic
        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            val routine = aList[nbr]
            routine.useMusic = isChecked
            updateItemRoutine(routine, nbr)
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

        ArrayAdapter.createFromResource(context, R.array.music_player, R.layout.spinner_text_item).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_text_item)
            playerMusicNameSpinner.adapter = adapter
            playerMusicNameSpinner.setSelection(
                context.resources.getStringArray(R.array.music_player).indexOf(aList[nbr].player)
            )
        }

        playerMusicNameSpinner.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (aList[nbr].player == playerMusicNameSpinner.selectedItem.toString())
                    return
                aList[nbr].player = playerMusicNameSpinner.selectedItem.toString()

                when (playerMusicNameSpinner.selectedItem.toString()) {
                    "Sleewell" -> {
                        nameMusicSelected.text = "night"
                        aList[nbr].musicName = "forest_night"
                        aList[nbr].musicUri = ""
                        aList[nbr].imagePlaylist = ""
                    }
                    "Spotify" -> {
                        nameMusicSelected.text = "Dormir profondément \uD83C\uDF19 Playlist de musique douce pour dormir au calme | Sons pour dormir"
                        aList[nbr].musicName = "Dormir profondément \uD83C\uDF19 Playlist de musique douce pour dormir au calme | Sons pour dormir"
                        aList[nbr].musicUri = "spotify:playlist:0rlLtbIl62xL3GKbGHPHP8"
                        aList[nbr].imagePlaylist = "https://i.scdn.co/image/ab67706c0000bebb3481226f8a68bd7262774a66"
                    }
                    else -> {
                        nameMusicSelected.text = "None"
                        aList[nbr].musicName = "None"
                        aList[nbr].musicUri = ""
                        aList[nbr].imagePlaylist = ""
                    }
                }
                updateItemRoutine(aList[nbr], nbr)
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
            playerMusicNameSpinner.visibility = View.VISIBLE
            selectMusic.visibility = View.VISIBLE
            nameMusicSelected.visibility = View.VISIBLE
        } else {
            musicIcon.setBackgroundResource(R.drawable.ic_music_off_blue)
            playerMusicNameSpinner.visibility = View.GONE
            selectMusic.visibility = View.GONE
            nameMusicSelected.visibility = View.GONE
        }
    }

    private fun setDialogHalo(dialog: Dialog, nbr: Int) {
        val haloSwitch = dialog.findViewById(R.id.dialog_routine_halo_switch) as SwitchCompat

        haloSwitch.isChecked = aList[nbr].useHalo
        haloSwitch.setOnCheckedChangeListener { _, isChecked ->
            aList[nbr].useHalo = isChecked
            updateItemRoutine(aList[nbr], nbr)
            setDialogHalo(dialog, nbr)
            setDialogColorSet(dialog, nbr)
        }
    }
}