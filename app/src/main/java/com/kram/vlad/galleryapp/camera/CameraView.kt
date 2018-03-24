package com.kram.vlad.galleryapp.camera

import android.content.Context
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.kram.vlad.galleryapp.Constants
import com.kram.vlad.galleryapp.models.ImageModel
import com.kram.vlad.galleryapp.utils.FileUtils
import com.kram.vlad.galleryapp.utils.Utils

/**
 * Created by vlad on 20.03.2018.
 * CameraPreview class
 */
class CameraView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null , defStyleAttr: Int = 0) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private val TAG = this::class.java.simpleName

    val pictureCallback = Camera.PictureCallback({ bytes: ByteArray, _: Camera ->
        val image = FileUtils().writeImage(bytes)
        camera.stopPreview()
        startPreview()

        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        options.inScaled = true
        Utils.images.add(0, ImageModel(image!!.name, image.path, BitmapFactory.decodeFile(image.path, options), 0, image.lastModified()))
        Utils.imagesUpdateManager.notify(Constants.UPDATE)
    })

    private lateinit var camera: Camera

    constructor(camera: Camera, context: Context) : this(context) {
        this.camera = camera
    }

    init {
        holder.addCallback(this)
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    companion object {
        private const val ROTATION_DEGREES = 90
    }

    override fun surfaceCreated(holder: SurfaceHolder?) = startPreview()
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        if (this.holder == null) {
            return
        }
        camera.stopPreview()
        startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera.stopPreview()
        camera.release()
    }

    private fun startPreview() {
        val params = camera.parameters
        params.setPictureSize(params.supportedPictureSizes[3].width, params.supportedPictureSizes[3].height)
        Log.d(TAG, params.pictureSize.width.toString() + "x" + params.pictureSize.height.toString())
        params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        params.setRotation(ROTATION_DEGREES)
        camera.parameters = params
        camera.setPreviewDisplay(this.holder)
        camera.setDisplayOrientation(ROTATION_DEGREES)
        camera.startPreview()
    }
}