package com.example.realestatecatalogkotlin.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.realestatecatalogkotlin.Helperss.Helpers
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.di.App
import com.example.realestatecatalogkotlin.presenters.MainPresenterActivity
import com.example.realestatecatalogkotlin.views.MainViewActivity
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : MvpAppCompatActivity(), MainViewActivity {

    @InjectPresenter
    lateinit var mainPresenterActivity: MainPresenterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appContext = applicationContext
        App.activity = this
        setToolbar()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (NavHostFragment.findNavController(navFragment).currentDestination?.label !=
            "fragment_login_fragment"
        ) {
            Helpers.stopTimer()
            Helpers.startTimer()

        } else if (NavHostFragment.findNavController(navFragment).currentDestination?.label ==
            "fragment_login_fragment"
        ) {
            Helpers.stopTimer()
        }
    }

    private fun setToolbar() {
        //не нашел как корректно убрать tilte у приложения
        toolbar.title = ""
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onResume() {
        Helpers.stopTimer()
        Helpers.startTimer()
        super.onResume()
    }

    override fun onDestroy() {
        if (isFinishing) {
            Helpers.closeApp()
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (NavHostFragment.findNavController(navFragment).currentDestination?.label ==
            "fragment_main_fragment"
        ) {
            AlertDialog.Builder(this).apply {
                setTitle("Закрыть приложение?")
                setPositiveButton("Да") { _, _ ->
                    Helpers.closeApp()
                }
                setNeutralButton("Выход из аккаунта") { _, _ ->
                    Helpers.signOut()
                }
                setNegativeButton("Нет") { _, _ ->
                }
            }.create().show()
        }
        super.onBackPressed()
    }
}
