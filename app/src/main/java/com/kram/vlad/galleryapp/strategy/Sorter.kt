package com.kram.vlad.galleryapp.strategy

/**
 * Created by vlad on 24.03.2018.
 * Sort images by some strategy
 */
class Sorter {
    var strategy: Strategy? = null
    fun sort() = strategy!!.sort()
}