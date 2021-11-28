package com.sleewell.sleewell.mvp.menu.profile.view

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.openWeather.Main
import com.sleewell.sleewell.api.sleewell.SleewellApiTracker
import com.sleewell.sleewell.modules.imageUtils.ImageUtils.Companion.cropToSquare
import com.sleewell.sleewell.modules.imageUtils.ImageUtils.Companion.getBitmapFromView
import com.sleewell.sleewell.modules.keyboardUtils.hideSoftKeyboard
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.profile.contract.ProfileContract
import com.sleewell.sleewell.mvp.menu.profile.presenter.ProfilePresenter
import com.sleewell.sleewell.mvp.menu.profile.view.dialogs.DeleteDialog
import com.sleewell.sleewell.mvp.menu.profile.view.dialogs.GivenImagesDialog
import com.sleewell.sleewell.mvp.menu.profile.view.dialogs.PickImageDialog
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.math.abs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sleewell.sleewell.mvp.menu.profile.view.dialogs.ProfileBottomSheet


class ProfileFragment : Fragment(), ProfileContract.View,
    PickImageDialog.DialogEventListener, GivenImagesDialog.DialogEventListener,
    DeleteDialog.DialogEventListener, ProfileBottomSheet.DialogEventListener {
    //Context
    private lateinit var presenter: ProfileContract.Presenter
    private lateinit var root: View

    private lateinit var dialogPick: DialogFragment
    private lateinit var dialogDelete: DialogFragment
    private var dialogGiven: DialogFragment? = null

    companion object {
        const val IMAGE_CAPTURE_CODE = 0
        const val IMAGE_PICK_CODE = 1
        var flagPickDialog = true
        var flagDeleteDialog = true
    }

    //View widgets
    private lateinit var progressWidget: ProgressBar
    private lateinit var pictureWidget: ImageView

    //Touch Detection
    private var mDownX: Float = 0f
    private var mDownY = 0f
    private val scrollThreshold: Float = 10f
    private var isOnClick = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false)

        if (MainActivity.accessTokenSleewell.isEmpty() && !MainActivity.getAccessGoogleAccount) {
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, LoginFragment())?.commit()
        } else {
            initActivityWidgets()
            setDialogListeners()
            setupUI(root.findViewById(R.id.profileParent))
            setPresenter(ProfilePresenter(this, this.activity as AppCompatActivity))
        }
        return root
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        val usernameInputWidget = root.findViewById<TextInputLayout>(R.id.usernameInputLayout)
        val firstNameInputWidget = root.findViewById<TextInputLayout>(R.id.firstNameInputLayout)
        val lastNameInputWidget = root.findViewById<TextInputLayout>(R.id.lastNameInputLayout)
        val emailInputWidget = root.findViewById<TextInputLayout>(R.id.emailInputLayout)

        progressWidget = root.findViewById(R.id.progress)
        pictureWidget = root.findViewById(R.id.avatar)

        val moreButtonWidget = root.findViewById<ImageButton>(R.id.buttonMore)
        val pictureButtonWidget = root.findViewById<View>(R.id.outlinePictureButton)
        val saveButtonWidget = root.findViewById<ImageButton>(R.id.buttonSave)

        dialogPick = PickImageDialog()
        dialogDelete = DeleteDialog()

        moreButtonWidget.setOnClickListener {
            ProfileBottomSheet().apply {
                show(this@ProfileFragment.requireActivity().supportFragmentManager, ProfileBottomSheet.TAG)
            }
        }

        pictureButtonWidget.setOnClickListener {
            if (!dialogPick.isAdded && flagPickDialog) {
                dialogPick.show(activity!!.supportFragmentManager, "Image picker")
                flagPickDialog = false
            }
        }

        saveButtonWidget.setOnClickListener {
            presenter.updateProfileInformation()
        }

        usernameInputWidget.editText?.doOnTextChanged { input, _, _, _ ->
            presenter.setUsername(input.toString())
        }
        firstNameInputWidget.editText?.doOnTextChanged { input, _, _, _ ->
            presenter.setFirstName(input.toString())
        }
        lastNameInputWidget.editText?.doOnTextChanged { input, _, _, _ ->
            presenter.setLastName(input.toString())
        }
        emailInputWidget.editText?.doOnTextChanged { input, _, _, _ ->
            presenter.setEmail(input.toString())
        }

        usernameInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            return@setOnEditorActionListener onEditorActionListener(actionId, keyEvent)
        }

        firstNameInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            return@setOnEditorActionListener onEditorActionListener(actionId, keyEvent)
        }

        lastNameInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            return@setOnEditorActionListener onEditorActionListener(actionId, keyEvent)
        }

        emailInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            return@setOnEditorActionListener onEditorActionListener(actionId, keyEvent)
        }
    }

    override fun updateProfileInfoWidgets(username: String, firstName: String, lastName: String, email: String) {
        usernameInputLayout?.editText?.setText(username)
        firstNameInputLayout?.editText?.setText(firstName)
        lastNameInputLayout?.editText?.setText(lastName)
        emailInputLayout?.editText?.setText(email)

        usernameInputLayout?.editText?.visibility = View.VISIBLE
        firstNameInputLayout?.editText?.visibility = View.VISIBLE
        lastNameInputLayout?.editText?.visibility = View.VISIBLE
        emailInputLayout?.editText?.visibility = View.VISIBLE

        usernameInputLayout?.visibility = View.VISIBLE
        firstNameInputLayout?.visibility = View.VISIBLE
        lastNameInputLayout?.visibility = View.VISIBLE
        emailInputLayout?.visibility = View.VISIBLE

        progressWidget.visibility = View.GONE
    }

    override fun logoutUser() {
        context?.let { it1 -> SleewellApiTracker.disconnect(it1) }
        presenter.logoutUser()
        fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, LoginFragment())?.commit()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    mDownX = event.x
                    mDownY = event.y
                    isOnClick = true
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    if (isOnClick) {
                        hideSoftKeyboard()
                    }
                }
                if (event.action == MotionEvent.ACTION_MOVE) {
                    if (isOnClick && (abs(mDownX - event.x) > scrollThreshold
                                || abs(mDownY - event.y) > scrollThreshold)) {
                        isOnClick = false
                    }
                }
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    override fun onDialogTakePictureClick(dialog: DialogFragment) {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, IMAGE_CAPTURE_CODE)
    }

    override fun onDialogPickPictureClick(dialog: DialogFragment) {
        val pickPhoto = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, IMAGE_PICK_CODE)
    }

    override fun onDialogGivenPictureClick(dialog: DialogFragment) {
        if (dialogGiven == null) {
            dialogGiven = GivenImagesDialog()
        }
        if (!dialogGiven!!.isAdded) {
            dialogGiven!!.show(activity!!.supportFragmentManager, "Image chooser")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                val bitmap = data.extras?.get("data") as Bitmap?
                if (bitmap != null) {
                    val cropImg = cropToSquare(bitmap)
                    pictureWidget.setImageBitmap(cropImg)
                    presenter.updateProfilePicture(cropImg)
                }
            }
        }
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            val selectedImage: Uri? = data?.data

            if(Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    activity?.contentResolver, selectedImage)
                val cropImg = cropToSquare(bitmap)
                pictureWidget.setImageBitmap(cropImg)
                presenter.updateProfilePicture(cropImg)
            } else {
                if (selectedImage != null) {
                    val source = ImageDecoder.createSource(activity!!.contentResolver, selectedImage)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    val cropImg = cropToSquare(bitmap)
                    pictureWidget.setImageBitmap(cropImg)
                    presenter.updateProfilePicture(cropImg)
                }
            }
        }
    }

    override fun onDialogPictureClick(picture: ImageView) {
        val bitmap = getBitmapFromView(picture) ?: return
        val cropImg = cropToSquare(bitmap)
        pictureWidget.setImageBitmap(cropImg)
        presenter.updateProfilePicture(cropImg)
    }

    override fun onContinue() {
        presenter.deleteAccount()
    }

    override fun onItem1Click() {
        logoutUser()
    }

    override fun onItem2Click() {
        dialogDelete.show(activity!!.supportFragmentManager, "Delete account")
    }

    private fun setDialogListeners() {
        (activity as MainActivity?)?.setPickDialogEventListener(this)
        (activity as MainActivity?)?.setGivenDialogEventListener(this)
        (activity as MainActivity?)?.setDeleteDialogEventListener(this)
        (activity as MainActivity?)?.setBottomSheetEventListener(this)
    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        (activity as MainActivity).setPickDialogEventListener(null)
        (activity as MainActivity).setGivenDialogEventListener(null)
        (activity as MainActivity).setDeleteDialogEventListener(null)
        (activity as MainActivity?)?.setBottomSheetEventListener(null)
    }

    override fun showToast(message: String) {
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun onEditorActionListener(actionId: Int, keyEvent: KeyEvent?): Boolean {
        if (keyEvent == null) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.updateProfileInformation()
                return true
            }
            return false
        }
        if (isDoneKeyPressed(actionId, keyEvent)) {
            presenter.updateProfileInformation()
            return true
        }
        return false
    }

    private fun isDoneKeyPressed(actionId: Int, keyEvent: KeyEvent): Boolean {
        return (actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.action == KeyEvent.ACTION_DOWN
                && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
    }

    override fun onPause() {
        super.onPause()
        if (this::presenter.isInitialized) {
            presenter.cancelHttpCall()
        }
    }
}