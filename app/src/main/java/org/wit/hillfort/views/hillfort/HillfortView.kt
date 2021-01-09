package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort.recyclerView2
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.BaseView
import kotlin.math.roundToInt

class HillfortView : BaseView(), AnkoLogger, ImageListener {

  lateinit var presenter: HillfortPresenter
  lateinit var map: GoogleMap

  var hillfort = HillfortModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)


    super.init(toolbarAdd, true)


    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      map = it
      presenter.doConfigureMap(map)
      it.setOnMapClickListener { presenter.doSetLocation() }
    }

    presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter


    val layoutManager = LinearLayoutManager(this)
    recyclerView2.layoutManager = layoutManager

    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
      presenter.doCheckVisited(isChecked)
    }

    chooseImage.setOnClickListener{
      presenter.cacheHillfort(hillfortTitle.text.toString(), description.text.toString(), notes.text.toString())
      presenter.doSelectImage()
    }

    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
      presenter.doSetRating(rating)
    }
  }

  @SuppressLint("ResourceAsColor")
  override fun showHillfort(hillfort: HillfortModel){
    if(hillfortTitle.text.isEmpty()) {
      hillfortTitle.setText(hillfort.title)
    }
    if(description.text.isEmpty()){
      description.setText(hillfort.description)
    }
    if(notes.text.isEmpty()){
        notes.setText(hillfort.notes)
      }
    checkBox.setChecked(hillfort.visited)
    if(hillfort.rating>0) {
      ratingBar.setRating(hillfort.rating)
    }
    if(hillfort.visited ==true){
      visitDate.setText("Date Visited: ${hillfort.date}")
    }

    showImages(hillfort.images)


    if (hillfort.images.size < 4) {
      chooseImage.setText(R.string.change_hillfort_image)
    }
    if(hillfort.images.size >3){
      chooseImage.setEnabled(false)
      chooseImage.setBackgroundColor(R.color.colorInactive)
    }
    this.showLocation(hillfort.location)
  }

  override fun showLocation(loc: Location) {
    lat.setText("%.6f".format(loc.lat))
    lng.setText("%.6f".format(loc.lng))
  }


  override fun onDeleteClick(image: String) {
    presenter.doDeleteImage(image)
  }

  override fun showImages (images: ArrayList<String>) {
    hillfort.images = images
    recyclerView2.adapter = ImageAdapter(images, this)
    recyclerView2.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)
    if(presenter.edit) menu.getItem(1).setVisible(true)
    return super.onCreateOptionsMenu(menu)
  }

  override fun showVisitDate(res: String){
    visitDate.setText(res)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_cancel -> {
        presenter.doCancel()
      }
      R.id.item_delete ->{
        presenter.doDelete()
      }
      R.id.item_save -> {
        if (hillfortTitle.text.toString().isEmpty()) {
          toast(R.string.enter_hillfort_title)
        } else {
          presenter.doAddOrSave(
            hillfortTitle.text.toString(),
            description.text.toString(),
            notes.text.toString()
          )
        }
      }
      R.id.item_share ->{
        presenter.doShareHillfort()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(data != null){
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    presenter.doCancel()
  }

  override fun onDestroy(){
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
    presenter.doResartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }

}