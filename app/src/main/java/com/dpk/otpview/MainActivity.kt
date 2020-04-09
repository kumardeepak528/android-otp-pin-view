package com.dpk.otpview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        otpView.setOnOtpEnteredListener (
            object : OTPListener{
                override fun otpFinished(otp: String?) {
                    Toast.makeText(this@MainActivity,otp,Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
