package org.wit.hillfort.views.settings

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.toolbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.email
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import org.wit.hillfort.views.hillfort.HillfortView
import org.wit.hillfort.views.hillfortlist.HillfortAdapter
import org.wit.hillfort.views.login.LoginView


class SettingsView : BaseView(), AnkoLogger {

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        super.init(toolbar, true)
        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter
        val user = FirebaseAuth.getInstance().currentUser

        if(user != null) {
            settingsEmail.setText(user.email)

        }

        presenter.doShowStats()

        btnUpdate.setOnClickListener(){

            val email = settingsEmail.text.toString()
            val password = settingsPassword.text.toString()
            if(email.length>0 && email != user?.email) {
                user!!.updateEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            toast("User email address updated.")
                        }
                    }
            }
            if(password.length>0) {
                user!!.updatePassword(password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            toast("User password updated.")
                        }
                    }
            }

        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_settings -> presenter.doSettings()
            R.id.item_map -> presenter.doShowHillfortsMap()
            R.id.item_logout -> presenter.doLogout()
            R.id.item_favourites -> presenter.doShowFavourites()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun showStats (stats: String) {
        userStats.setText(stats)
    }

    override fun onBackPressed() {
        presenter.doCancel()
    }


}



