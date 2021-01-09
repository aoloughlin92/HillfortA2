package org.wit.hillfort.views.hillfortlist

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW


class HillfortListPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doDeleteHillfort(id: String){
        var hillfort = app.hillforts.findByFbId(id)
        if(hillfort != null) {
            app.hillforts.delete(hillfort)
        }
        loadHillforts()
    }

    fun doFavourite(hillfort: HillfortModel){

    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT,0 ,"hillfort_edit", hillfort)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
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