package org.wit.hillfort.models

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun countVisited(): Int
    fun findByFbId(id:String) : HillfortModel?
    fun clear()
    fun setFavourite(hillfort: HillfortModel)
    fun findFavourites(): List<HillfortModel>
}