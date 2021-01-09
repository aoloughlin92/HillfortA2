package org.wit.hillfort.views.favourites

import org.wit.hillfort.views.favourites.FavouriteAdapter
import org.wit.hillfort.views.favourites.FavouritePresenter
import org.wit.hillfort.views.favourites.FavouriteListener
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.utils.SwipeToDeleteCallback
import org.wit.hillfort.views.BaseView


class FavouriteView : BaseView(), FavouriteListener {

    lateinit var presenter: FavouritePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        super.init(toolbar, true)
        presenter = initPresenter(FavouritePresenter(this)) as FavouritePresenter


        setSwipeRefresh()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadFavourites()


        val swipeDeleteHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as FavouriteAdapter
                deleteHillfort(viewHolder.itemView.tag as String)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(recyclerView)


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_settings -> presenter.doSettings()
            R.id.item_logout -> presenter.doLogout()
            R.id.item_map -> presenter.doShowHillfortsMap()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onHillfortClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onFavouriteClick(hillfort: HillfortModel, favourite: Boolean) {
        presenter.doFavourite(hillfort, favourite)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadFavourites()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = FavouriteAdapter(hillforts as ArrayList<HillfortModel>, this)
        recyclerView.adapter?.notifyDataSetChanged()
        checkSwipeRefresh()
    }

    fun deleteHillfort(id: String){
        info("Delete: deleteHillfort "+id)
        presenter.doDeleteHillfort(id)
    }

    fun setSwipeRefresh() {
        swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                swiperefresh.isRefreshing = true
                presenter.loadFavourites()

            }
        })
    }

    fun checkSwipeRefresh() {
        if (swiperefresh.isRefreshing) swiperefresh.isRefreshing = false
    }

    override fun onBackPressed() {
        presenter.doCancel()
    }
}

