package org.wit.hillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.helpers.*
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import java.util.*

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HillfortJSONStore : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HillfortModel> {
        return hillforts
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

    override fun create(hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serialize()
    }

    override fun update(hillfort: HillfortModel) {
        val hillfortsList = findAll() as ArrayList<HillfortModel>
        var foundHillfort: HillfortModel? = hillfortsList.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.images = hillfort.images
            foundHillfort.location = hillfort.location
            foundHillfort.date = hillfort.date
            foundHillfort.visited = hillfort.visited
            foundHillfort.notes = hillfort.notes
        }
        serialize()
    }



    override fun findByFbId(id:String) : HillfortModel? {
        val foundHillfort: HillfortModel? = hillforts.find { it.fbId == id }
        return foundHillfort
    }

    override fun setFavourite(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.favourite = hillfort.favourite
        }
    }

    override fun findFavourites(): List<HillfortModel> {
        val favourites = hillforts.filter{p-> p.favourite == true}
        return favourites
    }

    override fun clear() {
        hillforts.clear()
    }

    fun logAll() {
        hillforts.forEach{ info("${it}") }
    }

    override fun delete(hillfort: HillfortModel) {
        val foundHillfort: HillfortModel? = hillforts.find{it.id == hillfort.id}
        hillforts.remove(foundHillfort)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}