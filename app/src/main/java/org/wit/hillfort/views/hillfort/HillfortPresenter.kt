package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.hillfort.helpers.checkLocationPermissions
import org.wit.hillfort.helpers.createDefaultLocationRequest
import org.wit.hillfort.helpers.isPermissionGranted
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.*
import org.wit.hillfort.views.location.EditLocationView
import java.text.SimpleDateFormat
import java.util.*

class HillfortPresenter(view: BaseView): BasePresenter(view) , AnkoLogger {

    //val IMAGE_REQUEST = 1
    //val LOCATION_REQUEST = 2
    val sdf = SimpleDateFormat("dd/MMM/yyyy")

    var hillfort = HillfortModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    //var app: MainApp
    var edit = false;

    var map: GoogleMap? = null

    val locationRequest = createDefaultLocationRequest()

    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)

    init {
        //app = view.application as MainApp
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        }else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            // permissions denied, so use the default location
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }

    fun doAddOrSave(title: String, description: String, notes: String) {
        hillfort.title = title
        hillfort.description = description
        hillfort.notes = notes
        if (edit) {
            app.hillforts.update(hillfort.copy())
        } else {
            app.hillforts.create(hillfort.copy())
        }
        view?.finish()
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        app.hillforts.delete(hillfort)
        view?.finish()
    }

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doCheckVisited(isChecked: Boolean) {
        var res = "";
        if (isChecked) {
            hillfort.visited = true
            hillfort.date = sdf.format(Date())
            //view?.visitDate?.setText("Date Visited: ${hillfort.date}" )
            res = "Date Visited: ${hillfort.date}"
        } else {
            hillfort.visited = false
            hillfort.date = ""
            //view?.visitDate?.setText("")
        }
        view?.showVisitDate(res)

    }


    fun doSetLocation() {

        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.lat, hillfort.lng, hillfort.zoom))

        /*
        if (edit == false) {
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", defaultLocation)
        }
        else {
            view?.navigateTo(
                VIEW.LOCATION,
                LOCATION_REQUEST,
                "location",
                Location(hillfort.lat, hillfort.lng, hillfort.zoom)
            )
        }*/

        /*if (hillfort.zoom != 0f) {
            defaultLocation.lat = hillfort.lat
            defaultLocation.lng = hillfort.lng
            defaultLocation.zoom = hillfort.zoom
        }
        view.startActivityForResult(
            view.intentFor<EditLocationView>().putExtra("location", defaultLocation),
            LOCATION_REQUEST
        )

         */
    }

    fun cacheHillfort (title: String, description: String) {
        hillfort.title = title;
        hillfort.description = description
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                //hillfort.images = data.data.toString()
                hillfort.images.add(data.getData().toString())
                view?.showHillfort(hillfort)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                hillfort.lat = defaultLocation.lat
                hillfort.lng = defaultLocation.lng
                hillfort.zoom = defaultLocation.zoom
                locationUpdate(hillfort.lat, hillfort.lng)
            }
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        info("Running HillfortPresenter doConfigureMap")
        map = m
        locationUpdate(hillfort.lat, hillfort.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        info("Running HillfortPresenter Location Update")
        hillfort.lat = lat
        hillfort.lng = lng
        hillfort.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.lat, hillfort.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.lat, hillfort.lng), hillfort.zoom))
        view?.showHillfort(hillfort)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
}