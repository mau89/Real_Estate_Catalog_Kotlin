package com.example.realestatecatalogkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.realestatecatalogkotlin.Helperss.Helpers
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.presenters.LoginPresenter
import com.example.realestatecatalogkotlin.views.LoginViewFragment
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment(), LoginViewFragment {
    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginPresenter.currentUsers()
        Helpers.stopTimer()
        btn_login_in.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            txt_login_error.visibility = View.GONE
            loginPresenter.checkLogin(edit_login.text.toString(), edit_password.text.toString())
        }
        btn_sign_up.setOnClickListener {
            txt_login_error.visibility = View.GONE
            loginPresenter.singUp(edit_login.text.toString(), edit_password.text.toString())
        }
    }

    override fun loginOk() {
        Helpers.startTimer()
        progress_bar.visibility = View.GONE
        txt_login_error.visibility = View.GONE
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    override fun loginError(message: Int) {
        progress_bar.visibility = View.GONE
        txt_login_error.visibility = View.VISIBLE
        txt_login_error.setText(message)
    }

    override fun loginError(message: String) {
        progress_bar.visibility = View.GONE
        txt_login_error.visibility = View.VISIBLE
        txt_login_error.text = message
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_edit_property).isVisible = false
        menu.findItem(R.id.action_add_property).isVisible = false
        menu.findItem(R.id.action_exit).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }
}