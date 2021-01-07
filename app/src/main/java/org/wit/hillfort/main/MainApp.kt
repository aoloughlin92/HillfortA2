package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.*
import org.wit.hillfort.models.json.HillfortJSONStore
import org.wit.hillfort.models.json.UserJSONStore
import org.wit.hillfort.models.room.HillfortStoreRoom

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore
    lateinit var users: UserStore
    lateinit var currentUser:  UserModel

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortStoreRoom(applicationContext)
        //hillforts = HillfortJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
        info("Hillfort started")
    }
}