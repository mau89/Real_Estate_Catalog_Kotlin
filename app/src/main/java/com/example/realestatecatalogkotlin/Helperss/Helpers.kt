package com.example.realestatecatalogkotlin.Helperss

import android.os.CountDownTimer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.di.App
import com.google.firebase.auth.FirebaseAuth

object Helpers {
    private var timer: CountDownTimer? = null

    fun startTimer() {
        timer = object : CountDownTimer(600000, 1000) {
            override fun onFinish() {
                if (App.activity.hasWindowFocus()) {
                    signOut()
                } else {
                    closeApp()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
    }

    fun signOut() {
        FirebaseAuth.getInstance()
            .signOut()
        val navBuilder = NavOptions.Builder()
        val navOptions = navBuilder.setPopUpTo(R.id.nav_graph, true).build()
        Navigation.findNavController(
            App.activity,
            R.id.navFragment
        ).navigate(R.id.loginFragment, null, navOptions)
    }

    fun closeApp() {
        FirebaseAuth.getInstance()
            .signOut()
        App.activity.finish()
    }
}