package com.faithhub.epilego.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.faithhub.epilego.databinding.ActivitySplashBinding
import com.faithhub.epilego.other.Internet
import com.faithhub.epilego.other.LocalSharedPref
import com.faithhub.epilego.other.UXUtils
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var pref: LocalSharedPref

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (_: Exception) {

        }
    }

    override fun onStart() {
        super.onStart()
        println(Internet.userDevice)
        pref = LocalSharedPref.getInstance(this)!!
        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.isLoggedIn) {
                startActivity(
                    Intent(
                        this, MainActivity::class.java
                    )
                )
                UXUtils.animateSlideLeft(this)
                finish()
            } else {
                startActivity(
                    Intent(
                        this, LoginActivity::class.java
                    )
                )
                UXUtils.animateSlideRight(this)
                finish()
            }
        }, 2500L)
    }
}