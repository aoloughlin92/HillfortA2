package org.wit.hillfort.activities

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

class HillfortListPresenter(val view: HillfortListView) {

    var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun getHillforts() = app.hillforts.findAll()

    fun doAddHillfort() {
        view.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view.startActivityForResult(view.intentFor<HillfortView>().putExtra("hillfort_edit", hillfort), 0)
    }

    fun doLogout(){
        app.currentUser = UserModel()
        view.logoutUser()
    }

    fun doSettings(){
        view.startActivityForResult<SettingsActivity>(0)
    }

    fun doShowHillfortsMap() {
        view.startActivity<HillfortMapsActivity>()
    }
}