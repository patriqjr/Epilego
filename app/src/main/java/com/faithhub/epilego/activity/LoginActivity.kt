package com.faithhub.epilego.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faithhub.epilego.databinding.ActivityLoginBinding
import com.faithhub.epilego.other.UXUtils

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginActivityViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } finally {
            with(this@LoginActivity) {
                binding.apply {
                    val ctx = this@LoginActivity
                    viewModel.chkNight(ctx)
                    viewModel.night.observe(ctx) {
                        if (viewModel.night.value!!) {
                            lottieDayBg.root.visibility = View.GONE
                            lottieNightBg.root.visibility = View.VISIBLE
                        } else {
                            lottieDayBg.root.visibility = View.VISIBLE
                            lottieNightBg.root.visibility = View.GONE
                        }
                    }
                    createAccountBtn.setOnClickListener {
                        UXUtils.vibrate(ctx)
                        startActivity(Intent(ctx, RegisterActivity::class.java))
                        UXUtils.animateSlideLeft(ctx)
                    }
                }
            }
        }
    }
}