package com.faithhub.epilego.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.faithhub.epilego.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Internet {

    @JvmStatic
    val defMail = "ctyatsbyf@gmail.com"

    @JvmStatic
    fun isEmailValid(email: String): String {
        val emailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        return if (emailPattern.matches(email))
            email
        else
            defMail
    }

    @JvmStatic
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    @JvmStatic
    val faithHubUsersDatabase =
        FirebaseDatabase.getInstance().reference.child("FaithHubUsers")

    @JvmStatic
    val userDevice =
        StringBuilder().append(Build.HARDWARE ?: "unknownHardware", Build.ID ?: "0").toString()

    @JvmStatic
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivityManager != null) {
            val process = Runtime.getRuntime().exec("ping -c 1 www.google.com")
            val exitCode = process.waitFor()
            val capabilities =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                } else
                    null
            if (capabilities != null) {

                when {
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && exitCode == 0 -> return true
                }
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnected && exitCode == 0
            }
        }
        return false
    }

    @Synchronized
    fun updateUser(ctx: Context, number: String?, task: () -> Unit) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val usersRef = faithHubUsersDatabase.child(number!!)
                var listener: ValueEventListener? = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            if (snapshot.exists()) {
                                val user = snapshot.getValue(User::class.java)
                                LocalSharedPref.getInstance(ctx)?.saveUser(user)
                                task()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                }
                usersRef.addValueEventListener(listener!!)
                Handler(Looper.getMainLooper()).postDelayed({
                    usersRef.removeEventListener(listener!!)
                    listener = null
                }, 20000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun phoneNumber(mobileNumber: String): String {
        return mobileNumber.replaceFirst("0", "")
    }
}