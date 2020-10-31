package org.wit.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R

class HillfortActivity : AppCompatActivity(), AnkoLogger {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    info("Hillfort Activity started..")
    setContentView(R.layout.activity_hillfort)

    btnAdd.setOnClickListener() {
      val hillfortTitle = hillfortTitle.text.toString()
      if (hillfortTitle.isNotEmpty()) {
        info("add Button Pressed: $hillfortTitle")
      }
      else {
        toast ("Please Enter a title")
      }
    }

  }
}