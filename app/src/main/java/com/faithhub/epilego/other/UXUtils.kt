package com.faithhub.epilego.other

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.faithhub.epilego.R

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
            val effect = VibrationEffect.createOneShot(100, 60)
            vibrator.vibrate(effect)
        } else {
            vibrator.vibrate(100)
        }
    }

    @JvmStatic
    @Synchronized
    fun toastError(ctx: Context, title: String?, msg: String?) {

    }


    @JvmStatic
    fun toastInfo(ctx: Context, title: String, msg: String) {

    }

    @Synchronized
    fun toastInternet(ctx: Context) {

    }

    @JvmStatic
    fun toastWarning(ctx: Activity, title: String, msg: String) {

    }

    @JvmStatic
    fun toastSuccess(ctx: Context, title: String, msg: String) {

    }
}
