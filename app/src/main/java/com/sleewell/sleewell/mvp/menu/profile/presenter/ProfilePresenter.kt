package com.sleewell.sleewell.mvp.menu.profile.presenter

import android.content.Context
import com.sleewell.sleewell.api.sleewell.model.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.ResponseSuccess
import com.sleewell.sleewell.mvp.menu.profile.ProfileContract
import com.sleewell.sleewell.mvp.menu.profile.model.ProfileModel

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
    private var model: ProfileContract.Model = ProfileModel(context)

    private var username: String = ""
    private var firstName: String = ""
    private var lastName: String = ""

    override fun onViewCreated() {
        getProfileInformation()
    }

    override fun getProfileInformation() {
        model.getProfileInformation(object : ProfileContract.Model.OnProfileInfoListener {
            override fun onFinished(profileInfo: ProfileInfo) {
                setUsername(profileInfo.username)
                setFirstName(profileInfo.firstname)
                setLastName(profileInfo.lastname)
                view?.updateProfileInfoWidgets(username, firstName, lastName)
            }

            override fun onFailure(t: Throwable) {
                view?.showToast("Error: could not retrieve user information")
            }
        })
    }

    override fun updateProfileInformation() {
        model.updateProfileInformation(username, firstName, lastName,
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
}