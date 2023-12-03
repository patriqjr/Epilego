package com.faithhub.epilego.other

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class FirebaseInit : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}