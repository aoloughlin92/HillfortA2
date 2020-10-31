package org.wit.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class HillfortActivity : AppCompatActivity(), AnkoLogger {

  var hillfort = HillfortModel()
  lateinit var app : MainApp
  //val hillforts = ArrayList<HillfortModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    info("Hillfort Activity started..")
    setContentView(R.layout.activity_hillfort)
    app = application as MainApp

    btnAdd.setOnClickListener() {
      hillfort.title = hillfortTitle.text.toString()
      hillfort.description = description.text.toString()
      if (hillfort.title.isNotEmpty()) {
        app.hillforts.add(hillfort.copy())
        info("add Button Pressed: ${hillfort}")
        for(i in app.hillforts.indices){
          info("Hillfort[$i]: ${app.hillforts[i]}")
        }
      }
      else {
        toast ("Please Enter a title")
      }
    }

  }
}