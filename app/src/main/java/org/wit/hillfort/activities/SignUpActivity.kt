package org.wit.hillfort.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel


class SignUpActivity : AppCompatActivity(), AnkoLogger {

    var user = UserModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        app = application as MainApp

        btnSignUp.setOnClickListener(){
            var email = signupEmail.text.toString()
            val findByEmail = app.users.findByEmail(email)
            if (findByEmail != null) {
                toast(R.string.signup_failed)
            }
            else{
                var password = signupPassword.text.toString()
                info("Hillfort Sign up with email: $email and password: $password")
                var tempUser = UserModel()
                tempUser.email = email
                tempUser.password=password
                app.currentUser = app.users.signup(tempUser)
                setResult(AppCompatActivity.RESULT_OK)
                startActivityForResult<HillfortListActivity>(0)
            }
        }
    }

}
