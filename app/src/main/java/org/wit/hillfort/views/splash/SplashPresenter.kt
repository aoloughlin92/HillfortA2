package org.wit.hillfort.views.splash

import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class SplashPresenter(view: BaseView): BasePresenter(view) {


    fun doShowLogin(){
        view?.navigateTo(VIEW.LOGIN)
    }
}