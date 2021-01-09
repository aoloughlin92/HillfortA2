package org.wit.hillfort.models.mem

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class HillfortMemStore : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()

    override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll();

    }
    override fun countVisited(): Int {
        var count = 0
        for(hillfort in hillforts){
            if(hillfort.visited ==true){
                count ++
            }
        }
        return count
    }

    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.images = hillfort.images
            foundHillfort.location = hillfort.location
            logAll();
        }
    }

    fun logAll() {
        hillforts.forEach{ info("${it}") }
    }

    override fun findByFbId(id:String) : HillfortModel? {
        val foundHillfort: HillfortModel? = hillforts.find { it.fbId == id }
        return foundHillfort
    }

    override fun clear() {
        hillforts.clear()
    }
}