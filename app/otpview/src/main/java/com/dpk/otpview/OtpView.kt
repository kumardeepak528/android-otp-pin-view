package com.dpk.otpview

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import com.dpk.otpview.otp.R

public class OtpView: LinearLayout{

    private var length: Int = 0
    private var editTextArray : ArrayList<AppCompatEditText> = ArrayList()
    private var onOtpFinished: OTPListener? = null
    private var mCurrentlyFocusedEditText: AppCompatEditText? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val styles = context.obtainStyledAttributes(attrs, R.styleable.OtpView)
        length = styles.getInt(R.styleable.OtpView_otpLength, DEFAULT_LENGTH)
        generateViews(styles, attrs)
        styles.recycle()
    }

    private fun generateViews(styles: TypedArray, attrs: AttributeSet?) {
        gravity = Gravity.CENTER_HORIZONTAL
        for (index in 0 until length){
            val itemView = generateEditText(index)
            editTextArray?.add(itemView)
            addView(itemView)
        }
        setFocusListener()
        setOnTextChangeListener()
    }


    private fun generateEditText(index: Int): AppCompatEditText {
        val editText = AppCompatEditText(context)
        editText.height = Utils.getPixels(context, DEFAULT_HEIGHT).toFloat().toInt()
        editText.width = Utils.getPixels(context, DEFAULT_WIDTH).toFloat().toInt()
        val space = Utils.getPixels(context, DEFAULT_SPACE).toFloat().toInt()
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        if (space > 0) {
            layoutParams.setMargins(space, space, space, space)
        }
        layoutParams.gravity = Gravity.CENTER
        editText.id = index
        editText.layoutParams = layoutParams
        editText.gravity = Gravity.CENTER
        editText.maxLines = 1
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.isCursorVisible = false

        Utils.setLength(editText)
        return editText
    }



    private fun setFocusListener() {
        val onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                mCurrentlyFocusedEditText = v as AppCompatEditText
                mCurrentlyFocusedEditText?.setSelection(mCurrentlyFocusedEditText?.text!!.length)
            }
        for (otpEditText in editTextArray) {
            otpEditText.onFocusChangeListener = onFocusChangeListener
        }
    }

    private fun setOnTextChangeListener() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (mCurrentlyFocusedEditText!!.text!!.isNotEmpty()
                    && mCurrentlyFocusedEditText !== editTextArray[editTextArray.size - 1]) {
                    mCurrentlyFocusedEditText!!.focusSearch(View.FOCUS_RIGHT)
                        .requestFocus()
                } else if (mCurrentlyFocusedEditText!!.text!!.isNotEmpty()
                    && mCurrentlyFocusedEditText === editTextArray[editTextArray.size - 1]) {
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(windowToken, 0)
                    onOtpFinished?.otpFinished(getEnteredOtp())
                } else {
                    val currentValue = mCurrentlyFocusedEditText!!.text.toString()
                    if (currentValue.isEmpty() && mCurrentlyFocusedEditText!!.selectionStart <= 0) {
                        mCurrentlyFocusedEditText!!.focusSearch(View.FOCUS_LEFT)
                            .requestFocus()
                    }
                }
            }
        }
        for (otpEdiText in editTextArray) {
            otpEdiText.addTextChangedListener(textWatcher)
        }
    }

    public fun setOnOtpEnteredListener(onOtpFinished: OTPListener?) {
        this.onOtpFinished = onOtpFinished
    }

    private fun getEnteredOtp(): String? {
        val stringBuilder = StringBuilder()
        for (otpEditText in editTextArray) {
            stringBuilder.append(otpEditText.text.toString())
        }
        return stringBuilder.toString()
    }

    companion object{
        private const val DEFAULT_LENGTH = 4
        private const val DEFAULT_HEIGHT = 48
        private const val DEFAULT_WIDTH = 48
        private const val DEFAULT_SPACE = 1

    }

}
public interface OTPListener {
    fun otpFinished(otp: String?)
}