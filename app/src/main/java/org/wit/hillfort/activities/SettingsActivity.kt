package org.wit.hillfort.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.toolbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel


class SettingsActivity : AppCompatActivity(), AnkoLogger {


    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        app = application as MainApp
        val user = app.currentUser

        toolbar.title = title
        setSupportActionBar(toolbar)

        btnUpdate.setOnClickListener(){
            val email = settingsEmail.text.toString()
            val findByEmail = app.users.findByEmail(email)
            if (findByEmail != null && !email.equals(user.email)) {
                toast(R.string.update_failed)
            }
            else{
                val password = settingsPassword.text.toString()
                user.email = email
                user.password = password
                info("Hillfort User Updated with email: $email and password: $password")
                app.users.update(user)
                setResult(AppCompatActivity.RESULT_OK)
                startActivityForResult<HillfortListActivity>(0)
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<HillfortActivity>(0)
            R.id.item_settings -> startActivityForResult<SettingsActivity>(0)
            R.id.item_logout -> {
                app.currentUser = UserModel()
                startActivityForResult<WelcomeActivity>(0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


}