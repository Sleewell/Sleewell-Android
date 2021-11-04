package com.sleewell.sleewell.mvp.menu.profile.model

import android.content.Context
import android.util.Log
import com.sleewell.sleewell.api.sleewell.ApiClient
import com.sleewell.sleewell.api.sleewell.IUserApi
import com.sleewell.sleewell.api.sleewell.model.profile.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.profile.ResponseBody
import com.sleewell.sleewell.api.sleewell.model.profile.ResponseSuccess
import com.sleewell.sleewell.database.analyse.night.entities.Night
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseDbUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.IAnalyseDataManager
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.profile.contract.ProfileContract
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody


class ProfileModel(context: Context) : ProfileContract.Model, IAudioAnalyseRecordListener {
    private val tag = "ProfileModelMVP"
    private var api: IUserApi? = ApiClient.retrofit.create(IUserApi::class.java)
    private val dataManager: IAnalyseDataManager = AudioAnalyseDbUtils(context, this)

    override fun getProfileInformation(token: String, onProfileInfoListener: ProfileContract.Model.OnProfileInfoListener) {
        val call: Call<ProfileInfo>? = api?.getProfileInformation(token)

        call?.enqueue(object : Callback<ProfileInfo> {
            override fun onResponse(call: Call<ProfileInfo>, response: Response<ProfileInfo>) {
                val responseRes: ProfileInfo? = response.body()

                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                    onProfileInfoListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    onProfileInfoListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ProfileInfo>, t: Throwable) {
                // Log error here since request failed
                Log.e(tag, t.toString())
                onProfileInfoListener.onFailure(t)
            }
        })
    }

    override fun updateProfileInformation(token: String,
        username: String, firstName: String, lastName: String, email: String,
        onFinishedListener: ProfileContract.Model.OnUpdateProfileInfoListener
    ) {

        val call : Call<ResponseSuccess>? = api?.updateProfileInformation(token,
            username.toRequestBody("text/plain".toMediaTypeOrNull()),
            firstName.toRequestBody("text/plain".toMediaTypeOrNull()),
            lastName.toRequestBody("text/plain".toMediaTypeOrNull()),
            email.toRequestBody("text/plain".toMediaTypeOrNull())
        )

        call?.enqueue(object : Callback<ResponseSuccess> {
            override fun onResponse(
                call: Call<ResponseSuccess>,
                response: Response<ResponseSuccess>
            ) {
                val responseRes: ResponseSuccess? = response.body()

                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                // Log error here since request failed
                Log.e(tag, t.toString())
                onFinishedListener.onFailure(t)
            }
        })
    }

    override fun getProfilePicture(
        token: String,
        onFinishedListener: ProfileContract.Model.OnFinishedListener<ResponseBody>
    ) {
        val call = api?.getProfilePicture(token)

        call?.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseRes: ResponseBody? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })
    }

    override suspend fun uploadProfilePicture(token: String, picture: File): ResponseSuccess? {

        val fileBody = picture.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("picture", picture.name, fileBody)

        return api?.uploadProfilePicture(token, body)
    }

    override fun deleteAccount(
        token: String,
        onFinishedListener: ProfileContract.Model.OnFinishedListener<ResponseSuccess>
    ) {
        val call : Call<ResponseSuccess>? = api?.deleteAccount(token)

        call?.enqueue(object : Callback<ResponseSuccess> {
            override
            fun onResponse(call: Call<ResponseSuccess>,
                           response: Response<ResponseSuccess>) {

                val responseRes: ResponseSuccess? = response.body()

                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                // Log error here since request failed
                Log.e(tag, t.toString())
                onFinishedListener.onFailure(t)
            }
        })
    }

    /**
     * Logout the user from the API
     * @author Hugo Berthomé
     */
    override fun removeToken() {
        MainActivity.accessTokenSleewell = ""
    }

    /**
     * Delete all the data nights saved on the phone
     * @author Hugo Berthomé
     */
    override fun deleteAllNightData() {
        dataManager.getAvailableAnalyse()
    }

    override fun onAnalyseRecordEnd() {
        // Unused
    }

    override fun onReadAnalyseRecord(data: Array<AnalyseValue>, nightId: Long) {
        // Unused
    }

    override fun onAnalyseRecordError(msg: String) {
        // Unused
    }

    /**
     * Function called when received the list of available analyse
     *
     * @param analyses
     */
    override fun onListAvailableAnalyses(analyses: List<Night>) {
        analyses.forEach {
            dataManager.deleteAnalyse(it.uId)
        }
    }
}