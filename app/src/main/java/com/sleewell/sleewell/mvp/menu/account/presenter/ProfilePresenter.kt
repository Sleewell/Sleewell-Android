package com.sleewell.sleewell.mvp.menu.account.presenter

import android.content.Context
import com.sleewell.sleewell.api.sleewell.model.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.ResponseSuccess
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.account.contract.ProfileContract
import com.sleewell.sleewell.mvp.menu.account.model.ProfileModel

/**
 * Presenter for the Profile fragment, it will link the HomeView and the HomeModel
 *
 * @constructor Creates a presenter based on the Contract
 * @param view View that inherits the View from the Contract
 * @param context Context of the activity / view
 * @author Titouan Fiancette
 */
class ProfilePresenter(view: ProfileContract.View, context: Context) : ProfileContract.Presenter {

    private var view: ProfileContract.View? = view
    private var model: ProfileContract.Model = ProfileModel()

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

    override fun onDestroy() {
        view = null;
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
}