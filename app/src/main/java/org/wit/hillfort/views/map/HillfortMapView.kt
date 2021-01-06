package org.wit.hillfort.views.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortMapView : BaseView(), GoogleMap.OnMarkerClickListener  {

    lateinit var presenter: HillfortMapPresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        super.init(toolbar)
        //setSupportActionBar(toolbar)
        //toolbar.title = title
        //presenter = HillfortMapPresenter(this)
        presenter = initPresenter (HillfortMapPresenter(this)) as HillfortMapPresenter
        mapView.onCreate(savedInstanceState);

        //app = application as MainApp

        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadHillforts()
        }
    }

    override fun showHillfort(hillfort: HillfortModel){
        currentTitle.text = hillfort.title
        currentDescription.text = hillfort.description
        if(hillfort!!.images.size > 0) {
            currentImage.setImageBitmap(readImageFromPath(this, hillfort!!.images.get(0)))
        }
        else{
            currentImage.setImageBitmap(null)
        }
    }

    override fun showHillforts(hillforts: List<HillfortModel>) {
        presenter.doPopulateMap(map, hillforts)
    }

    /*fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        app.hillforts.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            map.setOnMarkerClickListener(this)
        }
    }

     */

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    /*override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val hillfort = app.hillforts.findById(tag)
        currentTitle.text = hillfort!!.title
        currentDescription.text = hillfort!!.description
        if(hillfort!!.images.size > 0) {
            currentImage.setImageBitmap(readImageFromPath(this, hillfort!!.images.get(0)))
        }
        else{
            currentImage.setImageBitmap(null)
        }
        return false
    }*/
    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }



}