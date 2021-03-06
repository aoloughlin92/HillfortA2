package org.wit.hillfort.views


import android.content.Intent


import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.views.settings.SettingsView
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.favourites.FavouriteView
import org.wit.hillfort.views.location.EditLocationView
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.views.hillfort.HillfortView
import org.wit.hillfort.views.hillfortlist.HillfortListView
import org.wit.hillfort.views.login.LoginView

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, HILLFORT, MAPS, LIST, LOGIN, SETTINGS, FAVOURITES
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, HillfortListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.HILLFORT -> intent = Intent(this, HillfortView::class.java)
            VIEW.MAPS -> intent = Intent(this, HillfortMapView::class.java)
            VIEW.LIST -> intent = Intent(this, HillfortListView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.FAVOURITES-> intent = Intent(this, FavouriteView::class.java)
            VIEW.LOGIN -> {
                intent = Intent(this, LoginView::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
        /*val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            toolbar.title = "${title}: ${user.email}"
        }

         */
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }

    fun doShareHillfort(hillfort: HillfortModel){
        val intent= Intent()
        var locationURL = "https://www.google.ie/maps/@"+hillfort.location.lat+","+hillfort.location.lng+","+hillfort.location.zoom+"z"
        intent.action=Intent.ACTION_SEND
        if(hillfort.images.size>0) {
            intent.putExtra(
                Intent.EXTRA_TEXT, "Hillfort Title" + hillfort.title +
                        "\nHillfort Description: " + hillfort.description +
                        "\nHillfort Image: " + hillfort.images.get(0)+
                        "\nHillfort Location: "+ locationURL
            )
        }
        else{
            intent.putExtra(
                Intent.EXTRA_TEXT, "Hillfort Title" + hillfort.title +
                        "\nHillfort Description: " + hillfort.description+
                        "\nHillfort Location: "+ locationURL
            )
        }
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showHillfort(hillfort: HillfortModel) {}
    open fun showHillforts(hillforts: List<HillfortModel>) {}
    open fun showLocation(location : Location) {}
    open fun showVisitDate(res: String){}
    open fun showLocation(latitude : Double, longitude : Double) {}
    open fun showProgress(){}
    open fun hideProgress(){}
    open fun showStats(stats: String){}
    open fun showImages(images: ArrayList<String>){}

}