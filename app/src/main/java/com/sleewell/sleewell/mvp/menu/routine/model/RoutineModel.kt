package com.sleewell.sleewell.mvp.menu.routine.model

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
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
import com.sleewell.sleewell.api.sleewell.model.*
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


class RoutineModel(context: Context) : RoutineContract.Model {

    private var context = context
    private var db: RoutineDao = RoutineDatabase.getDatabase(context).routineDao()
    private var adapter: RoutineListAdapter
    private var aList: ArrayList<Routine> = ArrayList()
    private lateinit var dialog: Dialog
    private var api : IRoutineApi? = ApiClient.retrofit.create(IRoutineApi::class.java)

    init {
        aList.clear()
        adapter = RoutineListAdapter(context, aList)
    }

    override fun createNewItemRoutine(id: Int) {
        val rt = Routine("", false, id, 48, 63, 159, false, 48, false, "None", "", "", RoutineState.NONE.ordinal)

        if (id == -1)
            rt.state = RoutineState.NEW.ordinal
        aList.add(rt)
        adapter.notifyDataSetChanged()
    }

    override fun removeNewItemRoutine(routine: Routine) {
        aList.remove(routine)
        adapter.notifyDataSetChanged()
    }

    override fun updateItemRoutine(routine: Routine, nbr: Int) {
        aList[nbr] = routine
        adapter.notifyDataSetChanged()
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

    override fun updateListViewRoutine(routines: RoutinesResponse?) {
        val routinesDb = db.getAllRoutine()
        val routinesApi: ArrayList<com.sleewell.sleewell.api.sleewell.model.Routine> = ArrayList()

        for (i in routines?.data?.indices!!) {
            routinesApi.add(routines.data[i])
        }

        aList.clear()
        for (i in routinesDb.indices) {
            when(routinesDb[i].state) {
                RoutineState.NEW.ordinal -> {
                    addRoutineApiSleewell(routinesDb[i])
                }
                RoutineState.UPDATE.ordinal -> {
                    // UPDATE ROUTINE
                }
                RoutineState.DELETE.ordinal -> {
                    if (routinesApi.isEmpty())
                        break
                    aList.add(routinesDb[i])
                    deleteRoutineApiSleewell(aList.size - 1)
                    val isRoutine = routinesApi.find { it.id == routinesDb[i].apiId }
                    routinesApi.removeAt(routinesApi.indexOf(isRoutine))
                }
            }
            db.deleteRoutine(routinesDb[i])
        }

        for (i in routinesApi.indices) {
            val id = routinesApi[i].id
            val initColor = Color.parseColor(routinesApi[i].color)
            val r = Color.red(initColor)
            val g = Color.green(initColor)
            val b = Color.blue(initColor)
            val halo = routinesApi[i].halo == 1
            val duration = routinesApi[i].duration
            val useMusic = routinesApi[i].usemusic == 1
            val musicName = routinesApi[i].musicName
            val musicUri = routinesApi[i].musicUri
            val player = routinesApi[i].player
            val name = routinesApi[i].name
            val state = RoutineState.NONE.ordinal

            val rt = Routine(name, false, id, r, g, b, halo, duration, useMusic, player, musicName, musicUri, state)
            aList.add(rt)
        }
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    override fun updateListViewOffLine() {
        val routines = db.getAllRoutine()

        aList.clear()
        for (i in routines.indices) {
            if (routines[i].state != RoutineState.DELETE.ordinal) {
                aList.add(routines[i])
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    override fun getRoutineApiSleewell() {
        val call : Call<RoutinesResponse>? = api?.getRoutines(MainActivity.accessTokenSleewell)
        val TAG = "GET-ROUTINE-API"

        CoroutineScope(Dispatchers.IO).launch {
            updateListViewOffLine()
        }
        call?.enqueue(object: Callback<RoutinesResponse> {
            override fun onResponse(call: Call<RoutinesResponse>, response: retrofit2.Response<RoutinesResponse>) {
                val responseRes: RoutinesResponse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : $response")
                } else {
                    Log.e(TAG, "Success")
                    CoroutineScope(Dispatchers.IO).launch {
                        updateListViewRoutine(responseRes)
                    }
                }
            }
            override fun onFailure(call: Call<RoutinesResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
            }
        })
    }

    fun deleteRoutineApiSleewell(i: Int) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val TAG = "DEL-ROUTINE-API"

        if (aList[i].apiId == -1)
            return
        builder.addFormDataPart("id", aList[i].apiId.toString())

        val requestBody: RequestBody = builder.build()
        val call : Call<DeleteRoutineResponse>? = api?.deleteRoutine(MainActivity.accessTokenSleewell, requestBody)

        call?.enqueue(object: Callback<DeleteRoutineResponse> {

            override fun onResponse(call: Call<DeleteRoutineResponse>, response: retrofit2.Response<DeleteRoutineResponse>) {
                val responseRes: DeleteRoutineResponse? = response.body()
                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                } else if (response.code() == 200) {
                    Log.e(TAG, "Success")
                    removeNewItemRoutine(aList[i])
                }
            }
            override fun onFailure(call: Call<DeleteRoutineResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
                val rt = aList[i]
                removeNewItemRoutine(aList[i])
                rt.state = RoutineState.DELETE.ordinal
                CoroutineScope(Dispatchers.IO).launch {
                    //val rt = Routine(aList[i].name, aList[i].isSelected, aList[i].apiId, aList[i].colorRed, aList[i].colorGreen, aList[i].colorBlue, aList[i].useHalo, aList[i].duration, aList[i].useMusic, aList[i].player, aList[i].musicName, aList[i].musicUri, aList[i].state)
                    db.deleteRoutine(rt)
                    db.addNewRoutine(rt)
                    CoroutineScope(Dispatchers.Main).launch {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    fun updateRoutineApiSleewell(routine: Routine) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val TAG = "UPDATE-ROUTINE-API"
        val hex = String.format("#%02x%02x%02x", routine.colorRed, routine.colorGreen, routine.colorBlue)
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
        val call : Call<UpdateRoutineResponse>? = api?.updateRoutine(MainActivity.accessTokenSleewell, requestBody)

        call?.enqueue(object: Callback<UpdateRoutineResponse> {

            override fun onResponse(call: Call<UpdateRoutineResponse>, response: retrofit2.Response<UpdateRoutineResponse>) {
                val responseRes: UpdateRoutineResponse? = response.body()
                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                } else if (response.code() == 200) {
                    Log.e(TAG, "Success")
                }
            }
            override fun onFailure(call: Call<UpdateRoutineResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
            }
        })
    }

    override fun addRoutineApiSleewell(routine: Routine) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val TAG = "ADD-ROUTINE-API"
        val hex = String.format("#%02x%02x%02x", routine.colorRed, routine.colorGreen, routine.colorBlue)
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
        val call : Call<AddRoutineResponse>? = api?.addRoutine(
            MainActivity.accessTokenSleewell,
            requestBody
        )

        call?.enqueue(object: Callback<AddRoutineResponse> {
            override fun onResponse(
                call: Call<AddRoutineResponse>,
                response: retrofit2.Response<AddRoutineResponse>
            ) {
                val responseRes: AddRoutineResponse? = response.body()
                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                } else if (response.code() == 200) {
                    Log.e(TAG, "Success")
                    createNewItemRoutine(responseRes.id)
                }
            }
            override fun onFailure(call: Call<AddRoutineResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
                createNewItemRoutine(-1)
            }
        })
    }

    fun saveRoutineFromList() {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in aList.indices) {
                if (aList[i].uId == 0.toLong()) {
                    val rt = Routine(aList[i].name, aList[i].isSelected, aList[i].apiId, aList[i].colorRed, aList[i].colorGreen, aList[i].colorBlue, aList[i].useHalo, aList[i].duration, aList[i].useMusic, aList[i].player, aList[i].musicName, aList[i].musicUri, aList[i].state)
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
            //if (aList.isNotEmpty())
            //    updateRoutineApiSleewell(aList[nbr])
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
        val buttonSelect = dialog.findViewById(R.id.dialog_routine_selected_button) as Button
        val buttonClose = dialog.findViewById(R.id.dialog_routine_close) as Button
        val title = dialog.findViewById(R.id.dialog_routine_title) as EditText

        buttonClose.setOnClickListener { dialog.dismiss() }
        buttonDelete.setOnClickListener {
            deleteRoutineApiSleewell(nbr)
            dialog.dismiss()
        }
        /*buttonSelect.setOnClickListener {
            aList[nbr].isSelected = true
            CoroutineScope(Dispatchers.IO).launch {
                updateSelectedItemRoutine(nbr)
            }
            dialog.dismiss()
        }*/

        if (aList[nbr].name.isEmpty()) {
            title.setText("Routine ${aList[nbr].apiId}")
        } else {
            title.setText(aList[nbr].name)
        }
        /*
        title.doAfterTextChanged {
            aList[nbr].name = title.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                updateItemRoutine(aList[nbr], nbr)
            }
        }
        */
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

            /*btn.setOnClickListener {
                val btnColor = (btn.background as ColorDrawable).color
                val routine = aList[nbr]
                routine.colorBlue = btnColor.blue
                routine.colorGreen = btnColor.green
                routine.colorRed = btnColor.red
                updateItemRoutine(routine, nbr)
            }*/
        }
    }

    private fun setDialogMusic(dialog: Dialog, nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment) {

        val musicSwitch = dialog.findViewById(R.id.dialog_routine_music_switch) as SwitchCompat
        val musicIcon = dialog.findViewById(R.id.dialog_routine_music_icon) as ImageView
        val musicTitle = dialog.findViewById(R.id.dialog_routine_music_title) as TextView
        val playerMusicNameSpinner = dialog.findViewById(R.id.playerMusicNameSpinner) as Spinner
        val selectMusic = dialog.findViewById(R.id.dialog_routine_selectMusic_button) as ImageView
        val nameMusicSelected = dialog.findViewById(R.id.musicNameSelectedDialog) as TextView

        musicSwitch.isChecked = aList[nbr].useMusic
        /*musicSwitch.setOnCheckedChangeListener {_, isChecked ->
            val routine = aList[nbr]
            routine.useMusic = isChecked
            updateItemRoutine(routine, nbr)
            setDialogMusic(dialog, nbr, fragmentManager, fragment)
        }*/
        selectMusic.setOnClickListener {
            if (aList[nbr].player == "Spotify") {
                val spotifyDialog = SpotifyFragment()
                spotifyDialog.setTargetFragment(fragment, 1)
                fragmentManager?.let {it -> spotifyDialog.show(it, aList[nbr].uId.toString()) }
            }
            if (aList[nbr].player == "Sleewell") {
                val musicDialog = MusicFragment()
                musicDialog.setTargetFragment(fragment, 1)
                fragmentManager?.let {it -> musicDialog.show(it, aList[nbr].uId.toString()) }
            }
        }

        ArrayAdapter.createFromResource(context, R.array.music_player, R.layout.spinner_text_item).also {adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_text_item)
            playerMusicNameSpinner.adapter = adapter
            playerMusicNameSpinner.setSelection(
                context.resources.getStringArray(R.array.music_player).indexOf(aList[nbr].player)
            )
        }

        playerMusicNameSpinner.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                /*if (aList[nbr].player == playerMusicNameSpinner.selectedItem.toString())
                    return
                aList[nbr].player = playerMusicNameSpinner.selectedItem.toString()
                nameMusicSelected.text = "None"
                aList[nbr].musicName = "None"
                aList[nbr].musicUri = ""
                updateItemRoutine(aList[nbr], nbr)
                */
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
        /*haloSwitch.setOnCheckedChangeListener {_, isChecked ->
            aList[nbr].useHalo = isChecked
            updateItemRoutine(aList[nbr], nbr)
            setDialogHalo(dialog, nbr)
        }*/

        if (aList[nbr].useHalo) {
            haloTitle.text = "Turn on the halo"
            haloTitle.visibility = View.VISIBLE
        } else {
            haloTitle.visibility = View.INVISIBLE
        }
    }
}