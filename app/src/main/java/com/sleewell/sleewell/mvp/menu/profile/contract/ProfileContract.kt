package com.sleewell.sleewell.mvp.menu.profile.contract

import android.graphics.Bitmap
import com.sleewell.sleewell.api.sleewell.model.profile.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.profile.ResponseBody
import com.sleewell.sleewell.api.sleewell.model.profile.ResponseSuccess
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import java.io.File

/**
 * Contract that defines all the functions that will interact with the user
 * @author Titouan Fiancette
 */
interface ProfileContract {
    interface Model {
        interface OnProfileInfoListener {
            fun onFinished(profileInfo: ProfileInfo)
            fun onFailure(t: Throwable)
        }

        /**
         * API call to retrieve user profile information
         * @param onProfileInfoListener Listener extension with wanted behaviour
         * @author Titouan Fiancette
         */
        fun getProfileInformation(token: String,
                                  onProfileInfoListener: OnProfileInfoListener)

        interface OnUpdateProfileInfoListener {
            fun onFinished(response: ResponseSuccess)
            fun onFailure(t: Throwable)
        }

        /**
         * API call to update user profile information
         * @param onFinishedListener Listener extension with wanted behaviour
         * @author Titouan Fiancette
         */
        fun updateProfileInformation(token:String,
            username: String, firstName: String, lastName: String, email: String,
            onFinishedListener: OnUpdateProfileInfoListener
        )

        /**
         * retrieve user profile picture
         * @author Titouan Fiancette
         */
        fun getProfilePicture(token:String, onFinishedListener: OnFinishedListener<ResponseBody>)

        interface OnFinishedListener<T> {
            fun onFinished(response: T)
            fun onFailure(t: Throwable)
        }

        suspend fun uploadProfilePicture(
            token: String, picture: File): ResponseSuccess?

        fun deleteAccount(token: String,
                          onFinishedListener: OnFinishedListener<ResponseSuccess>)

        /**
         * Delete the API token
         * @author Hugo Berthomé
         */
        fun removeToken()

        /**
         * Delete all the data nights saved on the phone
         * @author Hugo Berthomé
         */
        fun deleteAllNightData()
    }

    interface Presenter : BasePresenter {
        /**
         * Function to call at the creation of the view
         * @author Titouan Fiancette
         */
        fun onViewCreated()

        /**
         * retrieve user profile information
         * @author Titouan Fiancette
         */
        fun getProfileInformation()

        /**
         * update user profile information
         * @author Titouan Fiancette
         */
        fun updateProfileInformation()

        fun getProfilePicture()
        fun updateProfilePicture(picture: Bitmap)
        fun cancelHttpCall()

        fun deleteAccount()

        fun setUsername(username: String)
        fun setFirstName(firstName: String?)
        fun setLastName(lastName: String?)
        fun setEmail(email: String?)

        /**
         * Logout the user
         * @author hugo Berthomé
         */
        fun logoutUser()
    }

    interface View : BaseView<Presenter> {
        /**
         * Show a toast message tu the user
         * @param username
         * @author Titouan Fiancette
         */
        fun updateProfileInfoWidgets(
            username: String,
            firstName: String,
            lastName: String,
            email: String
        )

        fun setProfilePictureBitmap(url : String)

        fun logoutUser()

        /**
         * Show a toast message tu the user
         * @param message information to show user
         * @author Titouan Fiancette
         */
        fun showToast(message: String)
    }
}