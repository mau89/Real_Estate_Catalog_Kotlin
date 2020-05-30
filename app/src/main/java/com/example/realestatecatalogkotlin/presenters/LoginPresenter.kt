package com.example.realestatecatalogkotlin.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.views.LoginViewFragment
import com.google.firebase.auth.FirebaseAuth

@InjectViewState
class LoginPresenter : MvpPresenter<LoginViewFragment>() {

    var auth = FirebaseAuth.getInstance()

    fun checkLogin(login: String, password: String) {
        if (!inputErrorLogin(login)) {
            viewState.loginError(R.string.error_login)
        } else if (!inputErrorPassword(password)) {
            viewState.loginError(R.string.error_password)
        } else {
            auth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewState.loginOk()
                    } else {
                        viewState.loginError(R.string.error_login_password)
                    }
                }
        }
    }

    fun currentUsers() {
        if (auth.currentUser != null) {
            viewState.loginOk()
        }
    }

    private fun inputErrorPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun inputErrorLogin(login: String): Boolean {
        return login.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches()
    }

    fun singUp(login: String, password: String) {
        if (!inputErrorLogin(login)) {
            viewState.loginError(R.string.error_login)
        } else if (!inputErrorPassword(password)) {
            viewState.loginError(R.string.error_password)
        } else {
            auth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewState.loginOk()
                    } else {
                        viewState.loginError(task.exception?.message.toString())
                    }
                }
        }
    }
}

