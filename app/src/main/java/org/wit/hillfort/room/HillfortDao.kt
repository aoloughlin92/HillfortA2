package org.wit.hillfort.room

import androidx.room.*
import org.wit.hillfort.models.HillfortModel

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findAll(): List<HillfortModel>

    @Query("select * from HillfortModel where id = :id")
    fun findById(id: String): HillfortModel

    @Update
    fun update(hillfort: HillfortModel)

    @Delete
    fun deleteHillfort(hillfort: HillfortModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFavourite(hillfort: HillfortModel)

    @Query("select * from HillfortModel where favourite = 1")
    fun findFavourites(): List<HillfortModel>

    @Query("select * from HillfortModel where title like :query")
    fun findSearchResults(query: String): List<HillfortModel>
}