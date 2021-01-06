package org.wit.hillfort.views.hillfortlist

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.activities.SettingsActivity
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import org.wit.hillfort.views.hillfort.HillfortView

class HillfortListPresenter(view: BaseView): BasePresenter(view) {

    //var app: MainApp

    //init {
   //     app = view.application as MainApp
   // }

    //fun getHillforts() = app.hillforts.findAll()

    fun doAddHillfort() {
        //view?.startActivityForResult<HillfortView>(0)
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT,0 ,"hillfort_edit", hillfort)
        //view.startActivityForResult(view.intentFor<HillfortView>().putExtra("hillfort_edit", hillfort), 0)
    }

    fun doLogout(){
        app.currentUser = UserModel()
        //view.logoutUser()
        view?.navigateTo(VIEW.WELCOME)
    }

    fun doSettings(){
        view?.startActivityForResult<SettingsActivity>(0)
    }

    fun doShowHillfortsMap() {
        //view?.startActivity<HillfortMapView>()
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadHillforts(){
        view?.showHillforts(app.hillforts.findAll())
    }
}