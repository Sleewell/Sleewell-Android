package com.sleewell.sleewell.halo.Model

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.view.MotionEvent
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.sleewell.sleewell.R
import com.sleewell.sleewell.halo.MainContract
import kotlinx.android.synthetic.main.colorpicker.*

/**
 * this class is the model of the halo
 *
 * @param context current context of the app
 * @author gabin warnier de wailly
 */
class HaloModel(context: Context) : MainContract.Model {
    private var size: Int = 10
    private val context: Context = context
    private lateinit var bitmap: Bitmap
    private lateinit var color: ColorFilter

    /**
     * This method return the current size of the circle
     *
     * @return Int
     * @author gabin warnier de wailly
     */
    override fun getSizeOfCircle(): Int {
        return size
    }

    /**
     * This method return the current color of the circle
     *
     * @return ColorFilter
     * @author gabin warnier de wailly
     */
    override fun getColorOfCircle(): ColorFilter {
        return color
    }

    /**
     * This method upgrade the size of the circle
     *
     * @author gabin warnier de wailly
     */
    override fun upgradeSizeOfCircle() {
        if (size < 1000)
            size = size + 3
    }

    /**
     * This method reduce the size of the circle
     *
     * @author gabin warnier de wailly
     */
    override fun degradesSizeOfCircle() {
        if (size > 10 && size - 2 > 10)
            size = size - 2
    }

    /**
     * This method reset the size of the circle
     *
     * @author gabin warnier de wailly
     */
    override fun resetSizeOfCircle() {
        size = 10
    }

    /**
     * This method set up and return the color picker
     *
     * @return Dialog
     * @author gabin warnier de wailly
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun openColorPicker(): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.colorpicker)

        val colorImage = dialog.findViewById(R.id.colorImage) as ImageView
        val resultColor = dialog.findViewById(R.id.resultColor) as ImageView

        resultColor.setColorFilter(Color.rgb(0,0,255))
        resultColor.setBackgroundColor(Color.rgb(0,0,255))
        this.color = resultColor.colorFilter

        colorImage.isDrawingCacheEnabled = true
        colorImage.buildDrawingCache(true)
        
        colorImage.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                this.bitmap = colorImage.drawingCache
                if (event.y.toInt() < bitmap.height && event.x.toInt() < bitmap.width && event.y.toInt() > 0 && event.x.toInt() > 0) {
                    val pixel = bitmap.getPixel(event.x.toInt(), event.y.toInt())
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)
                    resultColor.setColorFilter(Color.rgb(r,g,b))
                    resultColor.setBackgroundColor(Color.rgb(r,g,b))
                    this.color = resultColor.colorFilter
                }
            }
            true
        }
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true);
        return dialog
    }
}