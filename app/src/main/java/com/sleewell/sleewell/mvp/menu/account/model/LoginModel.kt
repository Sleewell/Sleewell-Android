package com.sleewell.sleewell.mvp.menu.account.model

import android.content.Context
import android.util.Log
import com.sleewell.sleewell.mvp.menu.account.ApiClientSleewell
import com.sleewell.sleewell.mvp.menu.account.ApiInterfaceSleewell
import com.sleewell.sleewell.mvp.menu.account.ApiResultLoginSleewell
import com.sleewell.sleewell.mvp.menu.account.contract.LoginContract
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback


class LoginModel(context: Context) : LoginContract.Model {

    private var api : ApiInterfaceSleewell? = ApiClientSleewell.retrofit.create(ApiInterfaceSleewell::class.java)
    private val TAG = "ConnectionModel"

    override fun loginToSleewell(onFinishedListener: LoginContract.Model.OnFinishedListener, name : String, password: String) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder.addFormDataPart("login", name)
        builder.addFormDataPart("password", password)

        val requestBody: RequestBody = builder.build()
        val call : Call<ApiResultLoginSleewell>? = api?.loginSleewell(requestBody)

        Log.e(TAG, call?.request().toString())

        call?.enqueue(object : Callback<ApiResultLoginSleewell> {

            override fun onResponse(call: Call<ApiResultLoginSleewell>, response: retrofit2.Response<ApiResultLoginSleewell>) {
                val responseRes: ApiResultLoginSleewell? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    Log.e(TAG, "Success")
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ApiResultLoginSleewell>, t: Throwable) {
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })

    }
}