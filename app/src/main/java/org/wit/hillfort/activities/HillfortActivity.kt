package org.wit.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel

class HillfortActivity : AppCompatActivity(), AnkoLogger {

  var hillfort = HillfortModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    info("Hillfort Activity started..")
    setContentView(R.layout.activity_hillfort)

    btnAdd.setOnClickListener() {
      hillfort.title = hillfortTitle.text.toString()
      if (hillfort.title.isNotEmpty()) {
        info("add Button Pressed: $hillfort")
      }
      else {
        toast ("Please Enter a title")
      }
    }

  }
}