package com.faithhub.epilego.activity

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginActivityViewModel : ViewModel() {
    val night: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val todayText: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun chkNight(context: Context) {
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                night.value = false
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                night.value = true
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                night.value = false
            }
        }
    }
}