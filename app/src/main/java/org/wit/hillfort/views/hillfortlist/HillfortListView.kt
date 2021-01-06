package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.activities.WelcomeActivity
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.hillfort.HillfortView


class HillfortListView : BaseView(), HillfortListener {

    //lateinit var app: MainApp

    lateinit var presenter: HillfortListPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        //app = application as MainApp

        //toolbar.title = title
        setSupportActionBar(toolbar)

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter


        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadHillforts()
        //recyclerView.adapter = HillfortAdapter(presenter.getHillforts(),this)
        //recyclerView.adapter?.notifyDataSetChanged()
        //loadHillforts()

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()//startActivityForResult<HillfortView>(0)
            R.id.item_settings -> presenter.doSettings()//startActivityForResult<SettingsActivity>(0)
            R.id.item_logout -> { presenter.doLogout()
                /*
                app.currentUser = UserModel()
                var intent = Intent(this, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent);

                 */
            }
            R.id.item_up ->{
                finish()
            }
            R.id.item_map -> presenter.doShowHillfortsMap()//startActivity<HillfortMapsActivity>()

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onHillfortClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
        //startActivityForResult(intentFor<HillfortView>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*
    private fun loadHillforts() {
        //showHillforts(app.hillforts.findAll())
        showHillforts(presenter.getHillforts())
    }
    */

    override fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

/*

    fun logoutUser() {
        var intent = Intent(this, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent);
    }

     */
}

