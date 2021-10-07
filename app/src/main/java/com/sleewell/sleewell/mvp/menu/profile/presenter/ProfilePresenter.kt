package com.sleewell.sleewell.mvp.menu.profile.presenter

import android.content.Context
import android.graphics.Bitmap
import com.sleewell.sleewell.api.sleewell.model.profile.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.profile.ResponseSuccess
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.profile.contract.ProfileContract
import com.sleewell.sleewell.mvp.menu.profile.model.ProfileModel
import kotlinx.coroutines.*
import java.io.*

/**
 * Presenter for the Profile fragment, it will link the HomeView and the HomeModel
 *
 * @constructor Creates a presenter based on the Contract
 * @param view View that inherits the View from the Contract
 * @param context Context of the activity / view
 * @author Titouan Fiancette
 */
class ProfilePresenter(view: ProfileContract.View,val context: Context) : ProfileContract.Presenter {

    private var view: ProfileContract.View? = view
    private var model: ProfileContract.Model = ProfileModel(context)
    private var coroutine: Job? = null

    private var username: String = ""
    private var firstName: String = ""
    private var lastName: String = ""
    private var email: String = ""

    override fun onViewCreated() {
        getProfileInformation()
    }

    override fun getProfileInformation() {
        val token = "Bearer ${MainActivity.accessTokenSleewell}"

        model.getProfileInformation(token,
            object : ProfileContract.Model.OnProfileInfoListener {
            override fun onFinished(profileInfo: ProfileInfo) {
                setUsername(profileInfo.username)
                setFirstName(profileInfo.firstname)
                setLastName(profileInfo.lastname)
                setEmail(profileInfo.email)
                view?.updateProfileInfoWidgets(username, firstName, lastName, email)
            }

            override fun onFailure(t: Throwable) {
                view?.showToast("Error: could not retrieve user information")
            }
        })


    }

    override fun updateProfileInformation() {
        val token = MainActivity.accessTokenSleewell

        model.updateProfileInformation(token,
            username, firstName, lastName, email,
            object: ProfileContract.Model.OnUpdateProfileInfoListener {
                override fun onFinished(response: ResponseSuccess) {
                    view?.showToast("Saved")
                }

                override fun onFailure(t: Throwable) {
                    view?.showToast("Error: could not update user information")
                }
            })
    }

    override fun updateProfilePicture(picture: Bitmap) {
        val token = MainActivity.accessTokenSleewell

        //Convert bitmap to byte array
        coroutine = CoroutineScope(Job()).launch(Dispatchers.IO) {
            val file = File(context.cacheDir, "avatar.jpg")
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            picture.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos)
            val bitmapData = bos.toByteArray()

            //write the bytes in file
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                fos?.write(bitmapData)
                fos?.flush()
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                val response = model.uploadProfilePicture(token, file)

                withContext(Dispatchers.Main) {
                    if (response != null) {
                        view?.showToast("Avatar Saved")
                    } else {
                        view?.showToast("Error: could not upload your avatar")
                    }
                }
            } catch (e: retrofit2.HttpException) {
                withContext(Dispatchers.Main) {
                    view?.showToast("Error: could not upload your avatar")
                }
            }
        }
    }

    override fun logoutUser() {
        model.removeToken()
        model.deleteAllNightData()
    }

    override fun deleteAccount() {
        val token = MainActivity.accessTokenSleewell

        model.deleteAccount(token,
        object : ProfileContract.Model.OnFinishedListener<ResponseSuccess> {
            override fun onFinished(response: ResponseSuccess) {
                view?.logoutUser()
                view?.showToast("Account deleted")
            }

            override fun onFailure(t: Throwable) {
                view?.showToast("Error: could not delete the account")
            }
        })
    }

    override fun onDestroy() {
        view = null
    }

    override fun setUsername(username: String) { this.username = username }
    override fun setFirstName(firstName: String?) {
        if (firstName != null) { this.firstName = firstName }
    }

    override fun setLastName(lastName: String?) {
        if (lastName != null) { this.lastName = lastName }
    }

    override fun setEmail(email: String?) {
        if (email != null) { this.email = email }
    }



    override fun cancelHttpCall() {
        coroutine?.cancel()
    }


}