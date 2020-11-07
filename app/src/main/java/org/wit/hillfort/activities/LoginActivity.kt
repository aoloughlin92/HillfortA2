package org.wit.hillfort.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel


class LoginActivity : AppCompatActivity(), AnkoLogger {

    var user = UserModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp

        btnLogin.setOnClickListener(){
            var email = loginEmail.text.toString()
            val findByEmail = app.users.findByEmail(email)
            if (findByEmail != null) {
                user = findByEmail
                var password = loginPassword.text.toString()
                if(user.password.equals(password)) {
                    info("Hillfort Log in with email: $email and password: $password")
                    app.currentUser = user
                    setResult(AppCompatActivity.RESULT_OK)
                    startActivityForResult<HillfortListActivity>(0)
                }
                else {
                    toast (R.string.login_failed)
                }
            }
            else {
                toast (R.string.login_failed)
            }
        }
    }

}