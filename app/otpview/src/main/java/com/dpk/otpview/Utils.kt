package com.dpk.otpview

import android.content.Context
import android.text.InputFilter
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText

object Utils{
    internal fun getPixels(context: Context, valueInDp: Int): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp.toFloat(), r.displayMetrics)
        return px.toInt()
    }

    internal fun getPixels(context: Context, valueInDp: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, r.displayMetrics)
        return px.toInt()
    }
    internal fun setLength(editText: AppCompatEditText) {
        val maxLength = 1
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = InputFilter.LengthFilter(maxLength)
        editText.filters = fArray
    }

}