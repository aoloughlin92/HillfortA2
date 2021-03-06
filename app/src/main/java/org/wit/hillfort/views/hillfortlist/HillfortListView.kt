package org.wit.hillfort.views.hillfortlist

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
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



class HillfortListView : BaseView(), HillfortListener {

    lateinit var presenter: HillfortListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        super.init(toolbar, false)
        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter


        setSwipeRefresh()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadHillforts()


        val swipeDeleteHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as HillfortAdapter
                deleteHillfort(viewHolder.itemView.tag as String)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(recyclerView)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.getItem(0).setVisible(true)
        //return super.onCreateOptionsMenu(menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                info("Search has been submitted" + query)
                if (query != null) {
                    presenter.doReturnResults(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    //presenter.doReturnResults(newText)
                    //TODO("Not yet implemented")
                }
                return true
            }
        })
        searchView.setOnCloseListener(object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                presenter.loadHillforts()
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_settings -> presenter.doSettings()
            R.id.item_favourites -> presenter.doShowFavourites()
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
        presenter.loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts as ArrayList<HillfortModel>, this)
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
                presenter.loadHillforts()

            }
        })
    }



    fun checkSwipeRefresh() {
        if (swiperefresh.isRefreshing) swiperefresh.isRefreshing = false
    }


}

