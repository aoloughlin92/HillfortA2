package org.wit.hillfort.views.settings

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import org.wit.hillfort.views.hillfortlist.HillfortListView

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

    fun doUpdateSettings(email: String) {
        /*val findByEmail = app.users.findByEmail(email)
        val user = FirebaseAuth.getInstance().currentUser
        if (findByEmail != null && !email.equals(user)) {
            // todo toast(R.string.update_failed)
        }
        else{

            val password = settingsPassword.text.toString()
            user.email = email
            user.password = password
            info("Hillfort User Updated with email: $email and password: $password")
            app.users.update(user)
            setResult(AppCompatActivity.RESULT_OK)
            startActivityForResult<HillfortListView>(0)
            todo

        }
        //view?.navigateTo(VIEW.HILLFORT)*/
    }

    fun doShowStats(){
        doAsync {
            val countHillforts = app.hillforts.findAll().size
            val countVisited = app.hillforts.countVisited()
            var statsString = ""
            if (countHillforts > 0) {
                val percentage = (countVisited * 100) / countHillforts
                statsString = """Total Hillforts: ${countHillforts} 
            |Number Visited: ${countVisited}
            |Percentage Visited: $percentage%
        """.trimMargin()
            } else {
                statsString = """Total Hillforts: ${countHillforts} 
                |Number Visited: ${countVisited}
            """.trimMargin()
            }
            uiThread { view?.showStats(statsString) }
        }
    }

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun doSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doCancel() {
        view?.finish()
    }

}