package org.wit.hillfort.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import java.io.ByteArrayOutputStream
import java.io.File

class HillfortFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    override fun findSearchResults(query: String): List<HillfortModel> {
        val results = hillforts.filter{p-> p.title.contains(query)}
        return results
    }

    override fun findFavourites(): List<HillfortModel> {
        val favourites = hillforts.filter{p-> p.favourite == true}
        return favourites
    }

    override fun findByFbId(id: String): HillfortModel? {
        val foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId === id}
        return foundHillfort
    }

    override fun setFavourite(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.favourite = hillfort.favourite
        }
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
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
        val key = db.child("users").child(userId).child("hillforts").push().key
        key?.let {
            hillfort.fbId = key

            hillfort.images.forEach{
                if ((it.length) > 0 && (it[0] != 'h')) {
                    updateImage(hillfort, it)
                }
            }
            hillforts.add(hillfort)
            db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
        }
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.images = hillfort.images
            foundHillfort.rating = hillfort.rating
            foundHillfort.location = hillfort.location
            foundHillfort.notes = hillfort.notes
            foundHillfort.visited = hillfort.visited
            foundHillfort.date = hillfort.date
        }
        hillfort.images.forEach{
            if ((it.length) > 0 && (it[0] != 'h')) {
                updateImage(hillfort, it)
            }
        }
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
    }

    override fun delete(hillfort: HillfortModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }


    override fun clear() {
        hillforts.clear()
    }

    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(hillforts) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                hillfortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImage(hillfort: HillfortModel, image:String) {
        var index = hillfort.images.indexOf(image)
        if (image != "") {
            val fileName = File(image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        hillfort.images[index] = it.toString()
                        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
                    }
                }
            }
        }
    }
}
