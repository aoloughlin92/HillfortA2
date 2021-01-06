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
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortView : BaseView(), AnkoLogger, ImageListener {

  lateinit var presenter: HillfortPresenter
  lateinit var map: GoogleMap

  var hillfort = HillfortModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)
    init(toolbarAdd)

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      map = it
      presenter.doConfigureMap(map)
    }

    presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

    val layoutManager = LinearLayoutManager(this)
    recyclerView2.layoutManager = layoutManager

    /*
    if (intent.hasExtra("hillfort_edit")) {
      edit = true
      hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
      hillfortTitle.setText(hillfort.title)
      description.setText(hillfort.description)
      notes.setText(hillfort.notes)
      checkBox.setChecked(hillfort.visited)
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
      btnAdd.setText(R.string.save_hillfort)
    }

     */


    checkBox.setOnCheckedChangeListener ({ buttonView, isChecked ->
      /*
      if(isChecked) {

        hillfort.visited = true
        hillfort.date = sdf.format(Date())
        visitDate.setText("Date Visited: ${hillfort.date}")
      }
      else{
        hillfort.visited = false
        hillfort.date = ""
        visitDate.setText("")
      }
    */
      presenter.doCheckVisited(isChecked)
    })
   /*
    btnAdd.setOnClickListener() {

      var title = hillfortTitle.text.toString()
      var description = description.text.toString()
      var notes = notes.text.toString()

      if (title.isEmpty()) {
        toast (R.string.enter_hillfort_title)
      }else{
        presenter.doAddOrSave(title, description, notes)

        if(edit) {
          app.hillforts.update(hillfort.copy())
        }else{
          app.hillforts.create(hillfort.copy())
        }
        info("add Button Pressed: $hillfortTitle")
        setResult(AppCompatActivity.RESULT_OK)
        finish()


      }
    }


    chooseImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

    */
    chooseImage.setOnClickListener{
      presenter.cacheHillfort(hillfortTitle.text.toString(), description.text.toString())
      presenter.doSelectImage()
    }
    hillfortLocation.setOnClickListener{
      presenter.cacheHillfort(hillfortTitle.text.toString(), description.text.toString())
      presenter.doSetLocation()
    }

    /*
    hillfortLocation.setOnClickListener {
      val location = Location(52.245696, -7.139102, 15f)
      if (hillfort.zoom != 0f) {
        location.lat =  hillfort.lat
        location.lng = hillfort.lng
        location.zoom = hillfort.zoom
      }
      startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
    }

     */


  }

  @SuppressLint("ResourceAsColor")
  override fun showHillfort(hillfort: HillfortModel){

    hillfortTitle.setText(hillfort.title)
    description.setText(hillfort.description)
    notes.setText(hillfort.notes)
    checkBox.setChecked(hillfort.visited)
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
    //btnAdd.setText(R.string.save_hillfort)
  }

  override fun onDeleteClick(image: String) {
    hillfort.images.remove(image)
    showImages(hillfort.images)
  }

  fun showImages (images: List<String>) {
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
      R.id.item_up ->{
        presenter.doCancel()
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
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(data != null){
      presenter.doActivityResult(requestCode, resultCode, data)
    }
    /*when (requestCode) {
      IMAGE_REQUEST -> {
        if (data != null) {
          hillfort.images.add(data.getData().toString())
          hillfortImage.setImageBitmap(readImage(this, resultCode, data))
          //showImages(hillfort.images)
          //chooseImage.setText(R.string.change_hillfort_image)
        }
      }
      LOCATION_REQUEST -> {
        if (data != null) {
          val location = data.extras?.getParcelable<Location>("location")!!
          hillfort.lat = location.lat
          hillfort.lng = location.lng
          hillfort.zoom = location.zoom
        }
      }
    }*/
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
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }


}