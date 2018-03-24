package com.kram.vlad.galleryapp.observer

import android.content.Context
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.util.Log
import com.kram.vlad.galleryapp.Constants
import com.kram.vlad.galleryapp.SQLiteHelper
import com.kram.vlad.galleryapp.models.ImageModel
import com.kram.vlad.galleryapp.utils.Utils
import java.io.File


/**
 * Created by vlad on 11.03.2018.
 * Thread for image download
 */
class ImageThread(private val context: Context, private val threadNumber: Int, private val imagePath: String?) : Runnable {
    private val TAG = this::class.java.simpleName

    override fun run() {
        Log.d(TAG, "Start new thread $threadNumber")
        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        options.inScaled = true
        val sqLiteHelper = SQLiteHelper(context)
        val imageFile = File(imagePath)
        val name = Uri.parse(imagePath).lastPathSegment
        val likes = sqLiteHelper.checkImage(name)
        val image: ImageModel
        val bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath, options), Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)

        if (bitmap != null) {
            if (likes == -1) {
                image = ImageModel(name, imagePath!!, bitmap, 0, imageFile.lastModified())
                sqLiteHelper.addImage(image)
            } else {
                image = ImageModel(name, imagePath!!, bitmap, likes, imageFile.lastModified())
            }
            Utils.images.add(image)
        }

        Utils.completedThreads++

        Log.d(TAG, Utils.completedThreads.toString())
        if (Utils.completedThreads == Constants.COUNT_OF_THREADS - 1) {
            Utils.completedThreads = 0
            Utils.imagesUpdateManager.notify(Constants.ADD)
        }

        Log.d(TAG, "Thread Completed $threadNumber")
    }
}