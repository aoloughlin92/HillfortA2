package org.wit.hillfort.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp


class SignUpActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btnSignUp.setOnClickListener(){
            var email = signupEmail.text.toString()
            var passsword = signupPassword.text.toString()
            info("Hillfort Sign in with email: $email and password: $passsword")
            setResult(AppCompatActivity.RESULT_OK)
            startActivityForResult<HillfortListActivity>(0)
        }
    }

}
