package org.wit.hillfort.views.hillfortlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel

interface HillfortListener {
    fun onHillfortClick(hillfort: HillfortModel)
    fun onFavouriteClick(hillfort: HillfortModel,favourite: Boolean)
}

class HillfortAdapter constructor(
    private var hillforts: ArrayList<HillfortModel>,
    private val listener: HillfortListener
) :
    RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )

        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort,listener)
    }


    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            var favourite = hillfort.favourite
            if(favourite) {
                itemView.favouriteButton.setImageResource(android.R.drawable.btn_star_big_on);
            }
            itemView.tag = hillfort.fbId
            itemView.hillfortTitle.text = hillfort.title
            itemView.description.text = hillfort.description
            if(hillfort.images.size>0) {
                var imagestring = hillfort.images.first()
                /*itemView.imageIcon.setImageBitmap(
                    readImageFromPath(
                        itemView.context,
                        imagestring
                    )
                )

                 */
                Glide.with(itemView.context).load(imagestring).into(itemView.imageIcon);

            }
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
            itemView.favouriteButton.setOnClickListener {
                if(favourite == true){
                    itemView.favouriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    favourite = false;
                    listener.onFavouriteClick(hillfort, favourite)
                }
                else{
                    itemView.favouriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    favourite = true;
                    listener.onFavouriteClick(hillfort, favourite)
                }
            }

        }
    }
}
