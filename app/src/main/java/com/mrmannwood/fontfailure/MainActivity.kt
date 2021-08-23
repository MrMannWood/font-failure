package com.mrmannwood.fontfailure

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val console = findViewById<TextView>(R.id.console)

        FontsContractCompat.requestFont(
            this@MainActivity,
            FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Atkinson Hyperlegible",
                R.array.com_google_android_gms_fonts_certs
            ),
            object : FontsContractCompat.FontRequestCallback() {
                override fun onTypefaceRetrieved(typeface: Typeface) {
                    console.typeface = typeface
                    console.text = "Font Request Succeeded"
                }
                override fun onTypefaceRequestFailed(reason: Int) {
                    val r = when(reason) {
                        FAIL_REASON_PROVIDER_NOT_FOUND -> "FAIL REASON PROVIDER NOT FOUND"
                        FAIL_REASON_FONT_LOAD_ERROR -> "FAIL REASON FONT LOAD ERROR"
                        FAIL_REASON_FONT_NOT_FOUND -> "FAIL REASON FONT NOT FOUND"
                        FAIL_REASON_FONT_UNAVAILABLE -> "FAIL REASON FONT UNAVAILABLE"
                        FAIL_REASON_MALFORMED_QUERY -> "FAIL REASON MALFORMED QUERY"
                        FAIL_REASON_WRONG_CERTIFICATES -> "FAIL REASON SECURITY VIOLATION"
                        FAIL_REASON_SECURITY_VIOLATION -> "FAIL REASON SECURITY VIOLATION"
                        else -> "OTHER REASON $reason"
                    }

                    console.text = "Font Request Failed with reason:\n$r"
                }
            },
            Handler(Looper.getMainLooper())
        )
    }
}