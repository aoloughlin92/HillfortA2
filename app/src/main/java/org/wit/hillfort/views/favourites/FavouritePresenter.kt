package org.wit.hillfort.views.favourites

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW


class FavouritePresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doDeleteHillfort(id: String){
        var hillfort = app.hillforts.findByFbId(id)
        if(hillfort != null) {
            app.hillforts.delete(hillfort)
        }
        loadFavourites()
    }

    fun doFavourite(hillfort: HillfortModel, favourite: Boolean){
        hillfort.favourite = favourite
        app.hillforts.setFavourite(hillfort)
        loadFavourites()
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT,0 ,"hillfort_edit", hillfort)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doCancel(){
        view?.finish()
    }

    fun doSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadFavourites(){
        doAsync{

            val hillforts = app.hillforts.findFavourites()
            uiThread{
                view?.showHillforts(hillforts)
            }
        }
    }
}