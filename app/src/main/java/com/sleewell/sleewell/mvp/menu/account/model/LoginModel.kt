package com.sleewell.sleewell.mvp.menu.account.model

import android.content.Context
import android.util.Log
import com.sleewell.sleewell.mvp.menu.account.ApiClientSleewell
import com.sleewell.sleewell.api.sleewell.ILoginApi
import com.sleewell.sleewell.api.sleewell.model.ResultLoginSleewell
import com.sleewell.sleewell.mvp.menu.account.contract.LoginContract
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback


class LoginModel(context: Context) : LoginContract.Model {

    private var api : ILoginApi? = ApiClientSleewell.retrofit.create(ILoginApi::class.java)
    private val TAG = "ConnectionModel"

    override fun loginToSleewell(onFinishedListener: LoginContract.Model.OnFinishedListener, name : String, password: String) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder.addFormDataPart("login", name)
        builder.addFormDataPart("password", password)

        val requestBody: RequestBody = builder.build()
        val call : Call<ResultLoginSleewell>? = api?.loginSleewell(requestBody)

        Log.e(TAG, call?.request().toString())

        call?.enqueue(object : Callback<ResultLoginSleewell> {

            override fun onResponse(call: Call<ResultLoginSleewell>, response: retrofit2.Response<ResultLoginSleewell>) {
                val responseRes: ResultLoginSleewell? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    Log.e(TAG, "Success")
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ResultLoginSleewell>, t: Throwable) {
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })

    }
}