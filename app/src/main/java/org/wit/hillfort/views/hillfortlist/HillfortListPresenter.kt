package org.wit.hillfort.views.hillfortlist

import org.jetbrains.anko.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW


class HillfortListPresenter(view: BaseView): BasePresenter(view) {

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT,0 ,"hillfort_edit", hillfort)
    }

    fun doLogout(){
        app.currentUser = UserModel()
        view?.navigateTo(VIEW.WELCOME)
    }

    fun doSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadHillforts(){
        doAsync{
            val hillforts = app.hillforts.findAll()
            uiThread{
                view?.showHillforts(hillforts)
            }
        }
    }
}