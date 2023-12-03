package com.faithhub.epilego.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faithhub.epilego.other.UXUtils

class OtpActivityViewModel : ViewModel() {
    val night: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun chkNight(context: Context) {
        Log.w(TAG, "chkNight1")
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                Log.w(TAG, "chkNight2")
                night.value = false
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                Log.w(TAG, "chkNight3")
                night.value = true
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                Log.w(TAG, "chkNight4")
                night.value = false
            }
        }
    }
}