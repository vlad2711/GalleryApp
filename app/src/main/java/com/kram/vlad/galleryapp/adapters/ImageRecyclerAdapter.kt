package com.kram.vlad.galleryapp.adapters

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kram.vlad.galleryapp.Constants
import com.kram.vlad.galleryapp.R
import com.kram.vlad.galleryapp.SQLiteHelper
import com.kram.vlad.galleryapp.handlers.ImageSelectedHandler
import com.kram.vlad.galleryapp.observer.EventListener
import com.kram.vlad.galleryapp.utils.FileUtils
import com.kram.vlad.galleryapp.utils.Utils
import kotlinx.android.synthetic.main.item_gallery.view.*
import java.util.*


/**
 * Created by vlad on 18.03.2018.
 * Adapter for gallery RecyclerView
 */
class ImageRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder>(), EventListener {

    private val TAG = this::class.java.simpleName
    override fun update(eventType: String) {
        Log.d(TAG, eventType)

        if (Objects.equals(eventType, Constants.UPDATE)) (context as AppCompatActivity).runOnUiThread({ this.notifyDataSetChanged() })
        if (Objects.equals(eventType, Constants.ADD)) Utils.sortImages()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false))
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) = holder.bind(context, position, context as ImageSelectedHandler)
    override fun getItemCount() = Utils.images.size


    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, position: Int, handler: ImageSelectedHandler) {

            itemView.imageView.setImageBitmap(Utils.images[position].bitmap)
            itemView.tag = adapterPosition
            itemView.likes.text = Utils.images[position].likes.toString()
            itemView.imageView.setOnClickListener({ handler.onImageSelected(position) })
            itemView.like.setOnClickListener({
                itemView.likes.text = (Integer.parseInt(itemView.likes.text as String) + 1).toString()
                Utils.images[position].likes = Integer.parseInt(itemView.likes.text as String)
                SQLiteHelper(context).like(Integer.parseInt(itemView.likes.text as String), Utils.images[position].name)
                Utils.imagesUpdateManager.notify(Constants.UPDATE)
            })

            if (position + 3 == Utils.images.size) {
                Utils.from = Utils.to
                Utils.to = Utils.from + 10
                FileUtils().readImages(context)
            }
        }
    }
}