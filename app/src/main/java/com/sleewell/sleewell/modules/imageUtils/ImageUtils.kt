package com.sleewell.sleewell.modules.imageUtils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

class ImageUtils {
    companion object {
        fun cropToSquare(bitmap: Bitmap): Bitmap {
            val width: Int = bitmap.width
            val height: Int = bitmap.height
            var side = 0
            var x = 0
            var y = 0
            if (width > height) {
                x = (width - height) / 2
                side = height
            } else {
                y = (height - width) / 2
                side = width
            }
            return Bitmap.createBitmap(bitmap, x, y, side, side)
        }

        fun getBitmapFromView(view: View): Bitmap? {
            val bitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
    }
}