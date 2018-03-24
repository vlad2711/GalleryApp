package com.kram.vlad.galleryapp.activitys

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.kram.vlad.galleryapp.Constants
import com.kram.vlad.galleryapp.R
import com.kram.vlad.galleryapp.adapters.ImageRecyclerAdapter
import com.kram.vlad.galleryapp.handlers.ImageSelectedHandler
import com.kram.vlad.galleryapp.utils.FileUtils
import com.kram.vlad.galleryapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ImageSelectedHandler {
    private val TAG = this::class.java.simpleName

    private lateinit var adapter: ImageRecyclerAdapter
    private lateinit var menu: Menu

    private var savedInstanceState: Bundle? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu!!
        menuInflater.inflate(R.menu.menu, menu)
        menu.getItem(Utils.sortMode - 1).isEnabled = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA)
            this.savedInstanceState = savedInstanceState

        } else {
            userInterfaceInit()

            if (savedInstanceState == null) {
                Log.d(TAG, "file")
                FileUtils().readImages(this)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.date_in -> Utils.sortMode = Constants.SORT_DATE_INCREASE
            R.id.date_de -> Utils.sortMode = Constants.SORT_DATE_DECREASE
            R.id.like_in -> Utils.sortMode = Constants.SORT_LIKE_INCREASE
            R.id.like_de -> Utils.sortMode = Constants.SORT_LIKE_DECREASE
        }

        Utils.sortImages()

        for (i in 0 until menu.size()) menu.getItem(i).isEnabled = (i != Utils.sortMode - 1)

        return super.onOptionsItemSelected(item)
    }

    override fun onImageSelected(imageId: Int) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra("id", imageId)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSION_REQUEST_CODE) {
            userInterfaceInit()
            if (savedInstanceState == null) {
                Log.d(TAG, "file")
                FileUtils().readImages(this)
            }
        }
    }

    private fun userInterfaceInit() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        adapter = ImageRecyclerAdapter(this)
        gallery.layoutManager = GridLayoutManager(this, Constants.SPAN_COUNT)
        gallery.adapter = adapter

        Utils.imagesUpdateManager.subscribe(Constants.UPDATE, adapter)
        Utils.imagesUpdateManager.subscribe(Constants.ADD, adapter)

        camera.setOnClickListener({ startActivity(Intent(this@MainActivity, CameraActivity::class.java)) })
    }

    override fun onPause() {
        super.onPause()
        if (::adapter.isInitialized) {
            Utils.imagesUpdateManager.unsubscribe(Constants.UPDATE, adapter)
            Utils.imagesUpdateManager.unsubscribe(Constants.ADD, adapter)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            Utils.imagesUpdateManager.subscribe(Constants.UPDATE, adapter)
            Utils.imagesUpdateManager.subscribe(Constants.ADD, adapter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::adapter.isInitialized) {
            Utils.imagesUpdateManager.unsubscribe(Constants.UPDATE, adapter)
            Utils.imagesUpdateManager.unsubscribe(Constants.ADD, adapter)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions(vararg permissions: String) = ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE)
}