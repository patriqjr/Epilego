package com.faithhub.epilego.activity

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.faithhub.epilego.R
import com.faithhub.epilego.databinding.ActivityOtpBinding
import com.faithhub.epilego.other.UXUtils

class OtpActivity : AppCompatActivity() {
    private val viewModel: OtpActivityViewModel by viewModels()
    private val editTexts = mutableListOf<AppCompatEditText>()
    private lateinit var backPressedCallback: OnBackPressedCallback
    private lateinit var binding: ActivityOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                UXUtils.vibrate(this@OtpActivity)
            }
        }
        binding.apply {
            val ctx = this@OtpActivity
            viewModel.chkNight(ctx)
            viewModel.night.observe(ctx) {
                if (viewModel.night.value!!) {
                    Log.w(ContentValues.TAG, "chkNight5")
                    lottieDayBg.root.visibility = View.GONE
                    lottieNightBg.root.visibility = View.VISIBLE
                } else {
                    Log.w(ContentValues.TAG, "chkNight6")
                    lottieDayBg.root.visibility = View.VISIBLE
                    lottieNightBg.root.visibility = View.GONE
                }
            }
        }

        for (i in 1..6) {
            val editTextId = resources.getIdentifier("otp$i", "id", packageName)
            val editText = findViewById<AppCompatEditText>(editTextId)
            editTexts.add(editText)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        moveFocusToNextEditText(editText)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            editText.setOnKeyListener { _, keyCode, event ->
                handleBackspace(keyCode, event, editText)
            }

            editText.setOnEditorActionListener { _, actionId, _ ->
                actionId == EditorInfo.IME_ACTION_DONE
            }
        }
    }

    private fun moveFocusToNextEditText(editText: AppCompatEditText?) {
        val currentIndex = editTexts.indexOf(editText)
        if (currentIndex < editTexts.size - 1) {
            val nextEditText = editTexts[currentIndex + 1]
            nextEditText.requestFocus()
        }
    }

    private fun handleBackspace(
        keyCode: Int,
        event: KeyEvent,
        currentEditText: AppCompatEditText
    ): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
            val currentIndex = editTexts.indexOf(currentEditText)
            if (currentIndex > 0) {
                val previousEditText = editTexts[currentIndex - 1]
                if (currentIndex != 5) {
                    previousEditText.text?.clear()
                    previousEditText.requestFocus()
                } else {
                    currentEditText.text?.clear()
                    previousEditText.text?.clear()
                    previousEditText.requestFocus()
                }
            }
            return true
        }
        return false
    }
}