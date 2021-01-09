package org.wit.hillfort.models.room

import android.content.Context
import androidx.room.Room
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import org.wit.hillfort.room.Database
import org.wit.hillfort.room.HillfortDao

class HillfortStoreRoom(val context: Context) : HillfortStore {

    var dao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.hillfortDao()
    }

    override fun findAll(): List<HillfortModel> {
        return dao.findAll()
    }

    override fun findById(id: Long): HillfortModel? {
        return dao.findById(id)
    }

    override fun clear() {
    }

    override fun create(hillfort: HillfortModel) {
        dao.create(hillfort)
    }

    override fun update(hillfort: HillfortModel) {
        dao.update(hillfort)
    }

    override fun delete(hillfort: HillfortModel) {
        dao.deleteHillfort(hillfort)
    }

    override fun countVisited(): Int {
        var count = 0
        for(hillfort in this.findAll()){
            if(hillfort.visited){
                count ++
            }
        }
        return count
    }
}