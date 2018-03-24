package com.kram.vlad.galleryapp.utils

import android.content.Context
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import com.kram.vlad.galleryapp.Constants
import com.kram.vlad.galleryapp.observer.ImageThread
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * Created by vlad on 10.03.2018.
 * Utils for working with files
 */
class FileUtils {
    private val TAG = this::class.java.simpleName

    fun readImages(context: Context) {
        val paths = getImagesPath()
        val executor = ThreadPoolExecutor(Constants.COUNT_OF_THREADS, Constants.COUNT_OF_THREADS, //NUMBER_OF_CORES*2, NUMBER_OF_CORES*2,
                60L, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())

        for (i in 0 until paths.size) executor.execute(ImageThread(context, i, paths[i]))
    }

    fun writeImage(data: ByteArray): File? {
        val pictureFile = getOutputMediaFile()
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ")
            return null
        }

        val fos = FileOutputStream(pictureFile)
        fos.write(data)
        fos.close()

        return pictureFile
    }

    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + File.separator + "Camera")
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(Date())
        val mediaFile: File
        mediaFile = File(mediaStorageDir.path + File.separator
                + "IMG_" + timeStamp + ".jpg")

        return mediaFile
    }

    private fun getImagesPath(): ArrayList<String> {
        val dcim = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + File.separator + "Camera")
        val pics = dcim.listFiles()
        val count = pics.size
        val paths = ArrayList<String>()
        for (i in count - Utils.to until count - Utils.from) {
            if (i in 0..(count - 1)) {
                Log.d(TAG, pics[i].absolutePath)
                if (pics[i].absolutePath.contains(".jpg") || pics[i].absolutePath.contains(".png"))
                    paths.add(pics[i].absolutePath)
            }
        }

        return paths
    }

    /*
    private fun getImagesPath(context: Context): ArrayList<String> {
        val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.Media._ID
        //Stores all the images from the gallery in Cursor
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, orderBy)
        //Total number of images
        val count = cursor.count

        Log.d(TAG, count.toString())
        //Create an array to store path to all the images
        val arrPath = ArrayList<String>()

        for (i in count - Utils.to until count - Utils.from) {
            if (i in 0..(count - 1)) {
                cursor.moveToPosition(i)
                val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                //Store the path of the image
                if (cursor.getString(dataColumnIndex) != null) {
                    arrPath.add(cursor.getString(dataColumnIndex))
                }
            }
        }
        cursor.close()
        return arrPath
    }
*/

}