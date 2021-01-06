package org.wit.hillfort.views.hillfort

import android.content.Intent
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.intentFor
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.location.EditLocationView
import java.text.SimpleDateFormat
import java.util.*

class HillfortPresenter(val view: HillfortView) {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    val sdf = SimpleDateFormat("dd/MMM/yyyy")

    var hillfort = HillfortModel()
    var location = Location(52.245696, -7.139102, 15f)
    var app: MainApp
    var edit = false;

    init {
        app = view.application as MainApp
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        }
    }

    fun doAddOrSave(title: String, description: String, notes: String) {
        hillfort.title = title
        hillfort.description = description
        hillfort.notes = notes
        if (edit) {
            app.hillforts.update(hillfort)
        } else {
            app.hillforts.create(hillfort)
        }
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doDelete() {
        app.hillforts.delete(hillfort)
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(view, IMAGE_REQUEST)
    }

    fun doCheckVisited(isChecked: Boolean) {
        if (isChecked) {
            hillfort.visited = true
            hillfort.date = sdf.format(Date())
            view.visitDate.setText("Date Visited: ${hillfort.date}")
        } else {
            hillfort.visited = false
            hillfort.date = ""
            view.visitDate.setText("")
        }
    }


    fun doSetLocation() {
        if (hillfort.zoom != 0f) {
            location.lat = hillfort.lat
            location.lng = hillfort.lng
            location.zoom = hillfort.zoom
        }
        view.startActivityForResult(
            view.intentFor<EditLocationView>().putExtra("location", location),
            LOCATION_REQUEST
        )
    }

    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                //hillfort.images = data.data.toString()
                hillfort.images.add(data.getData().toString())
                view.showHillfort(hillfort)
            }
            LOCATION_REQUEST -> {
                location = data.extras?.getParcelable<Location>("location")!!
                hillfort.lat = location.lat
                hillfort.lng = location.lng
                hillfort.zoom = location.zoom
            }
        }
    }
}