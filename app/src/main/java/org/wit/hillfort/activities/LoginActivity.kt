package org.wit.hillfort.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.signupEmail
import kotlinx.android.synthetic.main.activity_signup.signupPassword
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.R


class LoginActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener(){
            var email = loginEmail.text.toString()
            var passsword = loginPassword.text.toString()
            info("Hillfort Log in with email: $email and password: $passsword")
            setResult(AppCompatActivity.RESULT_OK)
            startActivityForResult<HillfortListActivity>(0)
        }
    }

}