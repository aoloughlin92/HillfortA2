package org.wit.hillfort.views.hillfort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_images.view.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath


interface ImageListener {
    fun onDeleteClick(image: String)
}


class ImageAdapter constructor(
    private var images: List<String>,
    private val listener: ImageListener
) :
    RecyclerView.Adapter<ImageAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_images,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val image = images[holder.adapterPosition]
        holder.bind(image, listener)
    }

    override fun getItemCount(): Int = images.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(image: String, listener: ImageListener) {
            /*itemView.hillfortMultipleImages.setImageBitmap(
                readImageFromPath(
                    itemView.context,
                    image
                )
            )*/
            Glide.with(itemView.context).load(image).into(itemView.hillfortMultipleImages);
            itemView.imagedeletebutton.setOnClickListener() {listener.onDeleteClick(image)}
        }


    }
}
