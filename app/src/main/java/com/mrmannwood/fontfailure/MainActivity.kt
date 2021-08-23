package com.mrmannwood.fontfailure

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private val FONTS = arrayOf(
            "Roboto" to "https://fonts.google.com/specimen/Roboto",
            "Besley" to "https://fonts.google.com/specimen/Besley",
            "Atkinson Hyperlegible" to "https://fonts.google.com/specimen/Atkinson+Hyperlegible",
            "Urbanist" to "https://fonts.google.com/specimen/Urbanist",
            "Open Sans" to "https://fonts.google.com/specimen/Open+Sans",
            "Lato" to "https://fonts.google.com/specimen/Lato",
            "Oswald" to "https://fonts.google.com/specimen/Oswald",
            "Raleway" to "https://fonts.google.com/specimen/Raleway"
        )
    }

    private lateinit var console: TextView
    private lateinit var handler: Handler

    private var fontIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        console = findViewById(R.id.console)
        handler = Handler(Looper.getMainLooper())

        findViewById<Button>(R.id.button).setOnClickListener {
            requestNextFont()
        }

        requestNextFont()
    }

    private fun requestNextFont() {
        val font = getNextFont()
        FontsContractCompat.requestFont(
            this@MainActivity,
            FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                font,
                R.array.com_google_android_gms_fonts_certs
            ),
            makeFontRequestCallback(font),
            handler
        )
    }

    private fun getNextFont(): String {
        val f = FONTS[fontIndex].first
        fontIndex++
        if (fontIndex >= FONTS.size) {
            fontIndex = 0
        }
        return f
    }

    private fun makeFontRequestCallback(font: String) : FontsContractCompat.FontRequestCallback {

        return object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface) {
                console.typeface = typeface
                console.text = "Font Request for:\n$font\nSucceeded"
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

                console.text = "Font Request for:\n$font\nFailed with reason:\n$r"
            }
        }
    }
}