package com.kram.vlad.galleryapp.models

import android.graphics.Bitmap

/**
 * Created by vlad on 12.03.2018.
 * Models for storing image
 */
data class ImageModel(val name: String, val path: String, val bitmap: Bitmap, var likes: Int, val dateOfLastModified: Long)
