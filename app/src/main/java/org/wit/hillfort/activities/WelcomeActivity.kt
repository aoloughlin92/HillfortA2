package org.wit.hillfort.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp

class WelcomeActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        chooseLogin.setOnClickListener(){
            info("Hillfort Welcome Screen Option Login selected..")
            setResult(AppCompatActivity.RESULT_OK)
            startActivityForResult<LoginActivity>(0)
        }
        chooseSignUp.setOnClickListener(){
            info("Hillfort Welcome Screen Option Sign Up selected..")
            setResult(AppCompatActivity.RESULT_OK)
            startActivityForResult<SignUpActivity>(0)
        }

    }
}
