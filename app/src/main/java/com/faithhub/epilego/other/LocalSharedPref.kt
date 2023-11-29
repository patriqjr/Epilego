package com.faithhub.epilego.other

import android.content.Context
import android.content.SharedPreferences
import com.faithhub.epilego.models.User
import com.google.firebase.auth.FirebaseAuth

class LocalSharedPref(
    mCtx: Context
) {
    fun saveUser(user: User?) {
        val editor = sharedPreferences?.edit()
        editor?.putString("userNumber", user?.usrNum)
        editor?.putString("username", user?.usrname)
        editor?.putString("fullName", user?.fullName)
        editor?.putString("userImage", user?.img)
        editor?.putString("userLoggedDevice", user?.logDevice)
        editor?.putString("userIDNumber", user?.usrIDNum)
        editor?.putString("momoNumber", user?.momoNum)
        editor?.putString("momoNetwork", user?.momoNet)
        editor?.putString("walletBalance", user?.acctBal)
        editor?.putString("payMethod", user?.payMeth)
        editor?.putString("receiptMail", user?.rcptMail)
        editor?.putString("location", user?.location)
        editor?.putString("denom", user?.denom)
        editor?.apply()
    }

    fun getUserDetail(): User {
        return User(
            usrIDNum = sharedPreferences?.getString(
                "userIDNumber",
                FirebaseAuth.getInstance().uid ?: ""
            ),
            usrNum = sharedPreferences?.getString("userNumber", "null"),
            usrname = sharedPreferences?.getString("username", "null"),
            fullName = sharedPreferences?.getString("fullName", "Add full name"),
            denom = sharedPreferences?.getString("denom", "Add religious denomination"),
            img = sharedPreferences?.getString("userImage", ""),
            location = sharedPreferences?.getString("location", "Location not set"),
            logDevice = sharedPreferences?.getString("userLoggedDevice", Internet.userDevice),
            acctBal = sharedPreferences?.getString("walletBalance", "0.00"),
            payMeth = sharedPreferences?.getString("payMethod", ""),
            momoNet = sharedPreferences?.getString("momoNetwork", ""),
            momoNum = sharedPreferences?.getString("momoNumber", "Add payment number"),
            rcptMail = sharedPreferences?.getString("receiptMail", Internet.defMail)
        )
    }

    fun clear() {
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.putBoolean("first_time", true)
        editor?.apply()
    }

    val sharedPreferences: SharedPreferences? =
        mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)


    val isLoggedIn = sharedPreferences?.getString("userNumber", "") != ""

    companion object {
        const val SHARED_PREF_NAME = "epilego_prefs"
        var mInstance: LocalSharedPref? = null

        @Synchronized
        fun getInstance(mCtx: Context): LocalSharedPref? {
            if (mInstance == null) {
                mInstance = LocalSharedPref(mCtx)
            }
            return mInstance
        }
    }
}
