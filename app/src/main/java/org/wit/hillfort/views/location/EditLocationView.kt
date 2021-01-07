package org.wit.hillfort.views.location

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import org.wit.hillfort.R
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.BaseView


class EditLocationView : BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    //lateinit var map: GoogleMap
    lateinit var presenter: EditLocationPresenter
    //var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        super.init(toolbar)
        //location = intent.extras?.getParcelable<Location>("location")!!
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter

        /*
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment


        presenter = EditLocationPresenter(this)

         */
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            it.setOnMarkerDragListener(this)
            it.setOnMarkerClickListener(this)
            presenter.doConfigureMap(it)
        }
        /*
        mapFragment.getMapAsync{
            map = it
            map.setOnMarkerDragListener(this)
            map.setOnMarkerClickListener(this)
            presenter.doConfigureMap(map)
        }

         */
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_location, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.item_save -> {
                presenter.doSave()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Hillfort")
            .snippet("GPS : " + loc.toString())
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.setOnMarkerDragListener(this)
        map.setOnMarkerClickListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }*/

    override fun onMarkerClick(marker: Marker): Boolean {
        /*
        val loc = LatLng(location.lat, location.lng)
        marker.setSnippet("GPS : " + loc.toString())
        return false

         */
        presenter.doUpdateMarker(marker)
        return false
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
        lat.setText("%.6f".format(marker.position.latitude))
        lng.setText("%.6f".format(marker.position.longitude))
    }

    override fun onMarkerDragEnd(marker: Marker) {
        /*
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom

         */
        presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
    }

    override fun onBackPressed() {
        /*val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()*/
        presenter.doSave()
    }

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
}