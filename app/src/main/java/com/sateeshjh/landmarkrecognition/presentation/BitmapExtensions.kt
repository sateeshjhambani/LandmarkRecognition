package com.sateeshjh.landmarkrecognition.presentation

import android.graphics.Bitmap

fun Bitmap.centerCrop(requiredWidth: Int, requiredHeight: Int): Bitmap {
    val xStart = (width - requiredWidth) / 2
    val yStart = (height - requiredHeight) / 2

    if (xStart < 0 || yStart < 0 || requiredWidth > width || requiredHeight > height) {
        throw IllegalArgumentException("Invalid arguments for centerCrop")
    }

    return Bitmap.createBitmap(this, xStart, yStart, requiredWidth, requiredHeight)
}