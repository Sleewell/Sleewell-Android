package com.sleewell.sleewell.mvp.menu.account.model

import android.util.Log
import com.sleewell.sleewell.api.sleewell.ApiClient
import com.sleewell.sleewell.api.sleewell.IUserApi
import com.sleewell.sleewell.api.sleewell.model.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.ResponseSuccess
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.account.contract.ProfileContract
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileModel() : ProfileContract.Model {
    private val TAG = "ProfileModelMVP"
    private var api: IUserApi? = ApiClient.retrofit.create(IUserApi::class.java)

    override fun getProfileInformation(token: String, onProfileInfoListener: ProfileContract.Model.OnProfileInfoListener) {
        val call: Call<ProfileInfo>? = api?.getProfileInformation(token)

        call?.enqueue(object : Callback<ProfileInfo> {
            override fun onResponse(call: Call<ProfileInfo>, response: Response<ProfileInfo>) {
                val responseRes: ProfileInfo? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    onProfileInfoListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    onProfileInfoListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ProfileInfo>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
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
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })
    }
}