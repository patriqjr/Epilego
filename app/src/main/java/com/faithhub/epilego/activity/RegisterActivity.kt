package com.faithhub.epilego.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faithhub.epilego.databinding.ActivityRegisterBinding
import com.faithhub.epilego.other.Internet
import com.faithhub.epilego.other.LoadingDialog
import com.faithhub.epilego.other.UXUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterActivityViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prg: LoadingDialog

    private var usersDBListener: ValueEventListener? = null
    private var usersRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } finally {
            with(this@RegisterActivity) {
                binding.apply {
                    val ctx = this@RegisterActivity
                    prg = LoadingDialog(ctx)
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
                    loginBtn.setOnClickListener {
                        UXUtils.vibrate(ctx)
                        startActivity(
                            Intent(
                                ctx, LoginActivity::class.java
                            )
                        )
                        finish()
                        UXUtils.animateSlideRight(ctx)
                    }
                    createAccountBtn.setOnClickListener {
                        UXUtils.vibrate(ctx)
                        if (Internet.isOnline(this@RegisterActivity)) {
                            val rawNumber =
                                "+${ccpCode.selectedCountryCode}${regPhoneNum.text.toString()}"
                            val name = username.text.toString()
                            if (rawNumber.length < 13 || TextUtils.isEmpty(name) || name.length < 3) {
                                UXUtils.toast(
                                    createAccountBtn,
                                    "Phone number or name is too short. "
                                )
                            } else {
                                prg.show()
                                checkExist(Internet.phoneNumber(rawNumber), name, createAccountBtn)
                            }
                        } else
                            UXUtils.toastInternet(createAccountBtn)
                    }
                }
            }
        }
    }

    private fun checkExist(number: String, name: String, view: View) {
        binding.apply {
            val ctx = this@RegisterActivity
            if (Internet.isOnline(ctx)) {
                CoroutineScope(Dispatchers.IO + Internet.coroutineExceptionHandler).launch {
                    usersRef = Internet.usersDatabase.child(number)
                    usersDBListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                UXUtils.vibrate(this@RegisterActivity)
                                CoroutineScope(Dispatchers.Main + Internet.coroutineExceptionHandler).launch {
                                    prg.dismiss()
                                    UXUtils.toast(
                                        view,
                                        "An account is already using this number: $number"
                                    )
                                }
                            } else {
                                CoroutineScope(Dispatchers.Main + Internet.coroutineExceptionHandler).launch {
                                    UXUtils.toast(
                                        view,
                                        "$number available"
                                    )
                                    prg.dismiss()
                                    val intent =
                                        Intent(ctx, OtpActivity::class.java)
                                    intent.putExtra("number", number)
                                    intent.putExtra("name", name)
                                    intent.putExtra("login", false)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            CoroutineScope(Dispatchers.Main + Internet.coroutineExceptionHandler).launch {
                                prg.dismiss()
                                UXUtils.toast(
                                    view,
                                    "An unexpected error occurred: ${error.message}"
                                )
                            }
                        }
                    }
                    usersRef!!.addValueEventListener(usersDBListener!!)
                }
            } else {
                UXUtils.toastInternet(view)
                prg.dismiss()
            }
        }
    }

    override fun onStop() {
        super.onStop()
//        if (usersRef != null && (usersDBListener) != null) {
//            usersRef!!.removeEventListener(usersDBListener!!)
//        }
    }
}