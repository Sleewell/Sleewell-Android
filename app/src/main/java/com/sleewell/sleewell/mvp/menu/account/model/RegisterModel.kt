package com.sleewell.sleewell.mvp.menu.account.model

import android.content.Context
import android.util.Log
import com.sleewell.sleewell.mvp.menu.account.ApiClientSleewell
import com.sleewell.sleewell.api.sleewell.ILoginApi
import com.sleewell.sleewell.api.sleewell.model.ResultRegisterSleewell
import com.sleewell.sleewell.mvp.menu.account.contract.RegisterContract
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback

class RegisterModel(context: Context) : RegisterContract.Model {
    private var api : ILoginApi? = ApiClientSleewell.retrofit.create(ILoginApi::class.java)
    private val TAG = "RegisterModel"

    override fun registerToSleewellApi(onFinishedListener: RegisterContract.Model.OnFinishedListener, loginId: String, password: String, email: String, firstName: String, lastName: String) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder.addFormDataPart("login", loginId)
        builder.addFormDataPart("password", password)
        builder.addFormDataPart("email", email)
        builder.addFormDataPart("firstname", firstName)
        builder.addFormDataPart("lastname", lastName)

        val requestBody: RequestBody = builder.build()
        val call : Call<ResultRegisterSleewell>? = api?.registerSleewell(requestBody)

        Log.e(TAG, call?.request().toString())

        call?.enqueue(object : Callback<ResultRegisterSleewell> {

            override fun onResponse(call: Call<ResultRegisterSleewell>, response: retrofit2.Response<ResultRegisterSleewell>) {
                val responseRes: ResultRegisterSleewell? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    Log.e(TAG, "Success")
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ResultRegisterSleewell>, t: Throwable) {
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })
    }
}