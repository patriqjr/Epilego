package com.faithhub.epilego.other

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import com.faithhub.epilego.R
import com.google.android.material.snackbar.Snackbar

object UXUtils {

    @JvmStatic
    fun animateSlideRight(context: Context) {
        (context as Activity).overridePendingTransition(
            R.anim.animate_slide_in_left,
            R.anim.animate_slide_out_right
        )
    }

    @JvmStatic
    fun animateSlideLeft(context: Context) {
        (context as Activity).overridePendingTransition(
            R.anim.animate_slide_left_enter,
            R.anim.animate_slide_left_exit
        )
    }

    @JvmStatic
    @Synchronized
    fun vibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(70, 30)
            vibrator.vibrate(effect)
        } else {
            vibrator.vibrate(70)
        }
    }

    @JvmStatic
    @Synchronized
    fun toast(view: View, msg: String) {
        Snackbar.make(
            view, msg, Snackbar.LENGTH_LONG
        ).show()
    }


    @Synchronized
    fun toastInternet(view: View) {
        Snackbar.make(
            view, "Please check your internet connection and try again", Snackbar.LENGTH_LONG
        ).show()
    }
}
