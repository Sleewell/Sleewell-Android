package com.sleewell.sleewell.mvp.menu.profile.view

import android.R.attr.src
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
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.sleewell.SleewellApiTracker
import com.sleewell.sleewell.modules.keyboardUtils.hideSoftKeyboard
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.profile.contract.ProfileContract
import com.sleewell.sleewell.mvp.menu.profile.presenter.ProfilePresenter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.math.abs


class ProfileFragment : Fragment(),
    PickImageDialog.DialogEventListener, ProfileContract.View {
    //Context
    private lateinit var presenter: ProfileContract.Presenter
    private lateinit var root: View
    private lateinit var dialog: DialogFragment

    companion object {
        const val IMAGE_CAPTURE_CODE = 0
        const val IMAGE_PICK_CODE = 1
    }

    //View widgets
    private lateinit var progressWidget: ProgressBar
    private lateinit var pictureWidget: ImageView

    //Touch Detection
    private var mDownX: Float = 0f
    private var mDownY = 0f
    private val SCROLL_THRESHOLD: Float = 10f
    private var isOnClick = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false)

        if (MainActivity.accessTokenSleewell.isEmpty()) {
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, LoginFragment())?.commit()
        } else {
            initActivityWidgets()
            (activity as MainActivity?)?.setDialogEventListener(this)
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

        val pictureButtonWidget = root.findViewById<View>(R.id.outlinePictureButton)
        val saveButtonWidget = root.findViewById<ImageButton>(R.id.buttonSave)
        val logoutButtonWidget = root.findViewById<ImageButton>(R.id.buttonLogout)

        dialog = PickImageDialog()
        pictureButtonWidget.setOnClickListener {
            if (!dialog.isAdded) {
                dialog.show(activity!!.supportFragmentManager, "Image picker")
            }
        }

        saveButtonWidget.setOnClickListener {
            presenter.updateProfileInformation()
        }

        logoutButtonWidget.setOnClickListener {
            context?.let { it1 -> SleewellApiTracker.disconnect(it1) }
            presenter.logoutUser()
            fragmentManager?.beginTransaction()?.replace(R.id.nav_menu, LoginFragment())?.commit()
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
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.updateProfileInformation()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        firstNameInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.updateProfileInformation()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {
                presenter.updateProfileInformation()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        lastNameInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.updateProfileInformation()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {
                presenter.updateProfileInformation()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        emailInputWidget.editText?.setOnEditorActionListener { _, actionId, keyEvent ->
            if (keyEvent == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.updateProfileInformation()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            if (isDoneKeyPressed(actionId, keyEvent)) {
                presenter.updateProfileInformation()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
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
                    if (isOnClick && (abs(mDownX - event.x) > SCROLL_THRESHOLD
                                || abs(mDownY - event.y) > SCROLL_THRESHOLD)) {
                        isOnClick = false
                    }
                }
                v.performClick()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                val bitmap = data.extras?.get("data") as Bitmap?
                if (bitmap != null) {
                    val cropImg = cropToSquare(bitmap)
                    pictureWidget.setImageBitmap(cropImg)
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
            } else {
                if (selectedImage != null) {
                    val source = ImageDecoder.createSource(activity!!.contentResolver, selectedImage)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    val cropImg = cropToSquare(bitmap)
                    pictureWidget.setImageBitmap(cropImg)
                }
            }
        }
    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        (activity as MainActivity).setDialogEventListener(null)
    }

    override fun showToast(message: String) {
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun isDoneKeyPressed(actionId: Int, keyEvent: KeyEvent): Boolean {
        return (actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.action == KeyEvent.ACTION_DOWN
                && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val width: Int = bitmap.width
        val height: Int = bitmap.height
        var x = 0
        var y = 0
        if (width > height) {
            x = (width - height) / 2
        } else {
            y = (height - width) / 2
        }
        return Bitmap.createBitmap(bitmap, x, y, width, width)
    }
}