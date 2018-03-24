package com.kram.vlad.galleryapp.activitys

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kram.vlad.galleryapp.Constants
import com.kram.vlad.galleryapp.R
import com.kram.vlad.galleryapp.SQLiteHelper
import com.kram.vlad.galleryapp.utils.Utils
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        userInterfaceInit()
    }

    private fun userInterfaceInit() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        val position = intent.getIntExtra("id", 0)
        image.setImageURI(Uri.parse(Utils.images[position].path))
        image_likes.text = Utils.images[position].likes.toString()
        image_like.setOnClickListener({
            image_likes.text = (Integer.parseInt(image_likes.text as String) + 1).toString()
            Utils.images[position].likes = Integer.parseInt(image_likes.text as String)
            SQLiteHelper(this).like(Integer.parseInt(image_likes.text as String), Utils.images[position].name)
            Utils.imagesUpdateManager.notify(Constants.UPDATE)
        })

        back.setOnClickListener({ finish() })
    }
}
