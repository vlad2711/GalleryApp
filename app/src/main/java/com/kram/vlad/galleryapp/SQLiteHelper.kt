package com.kram.vlad.galleryapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.kram.vlad.galleryapp.models.ImageModel

/**
 * Created by vlad on 13.03.2018.
 * Do all jab with sqlite
 */
class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val TAG = this::class.java.simpleName

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "images"

        const val IMAGE_TABLE_NAME = "images"
        const val KEY_NAME = "name"
        const val KEY_PATH = "path"
        const val KEY_LIKES = "likes"
        const val KEY_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(TAG, "create")
        val CREATE_IMAGES_TABLE = "CREATE TABLE $IMAGE_TABLE_NAME($KEY_NAME TEXT, $KEY_PATH TEXT, $KEY_LIKES INTEGER, $KEY_DATE LONG)"
        db!!.execSQL(CREATE_IMAGES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $IMAGE_TABLE_NAME")
        onCreate(db)
    }

    fun addImage(imageModel: ImageModel) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_LIKES, imageModel.likes)
        values.put(KEY_PATH, imageModel.path)
        values.put(KEY_NAME, imageModel.name)
        values.put(KEY_DATE, imageModel.dateOfLastModified)

        db.insert(IMAGE_TABLE_NAME, null, values)
        db.close()
    }

    fun like(newLikes: Int, name: String) {
        val db = this.writableDatabase
        Log.d(TAG, newLikes.toString())
        val values = ContentValues()

        values.put(KEY_LIKES, newLikes)
        db.update(IMAGE_TABLE_NAME, values, "$KEY_NAME =?", arrayOf(name))
        db.close()
    }

    fun checkImage(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.query(IMAGE_TABLE_NAME, arrayOf(KEY_NAME, KEY_PATH, KEY_LIKES), "$KEY_NAME=?",
                arrayOf(name), null, null, null)

        return if (cursor.count == 0) {
            cursor.close()
            -1
        } else {
            cursor.moveToFirst()
            val likes = cursor.getInt(2)
            Log.d(TAG, likes.toString())
            cursor.close()
            likes
        }
    }
}