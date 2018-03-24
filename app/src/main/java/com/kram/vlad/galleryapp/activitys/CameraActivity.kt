package com.kram.vlad.galleryapp.activitys

import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kram.vlad.galleryapp.R
import com.kram.vlad.galleryapp.camera.CameraView
import com.kram.vlad.galleryapp.utils.Utils
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {
    private val camera: Camera = Camera.open()
    private lateinit var cameraView: CameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        userInterfaceInit()
    }

    private fun userInterfaceInit() {
        cameraView = CameraView(camera, this)
        camera_preview.addView(cameraView)
        take_photo.setOnClickListener({
            camera.takePicture(null, null, cameraView.pictureCallback)
            Utils.sortImages()
        })

        back.setOnClickListener({ finish() })
    }
}