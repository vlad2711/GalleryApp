package com.kram.vlad.galleryapp.utils

import com.kram.vlad.galleryapp.Constants
import com.kram.vlad.galleryapp.models.ImageModel
import com.kram.vlad.galleryapp.observer.ImagesUpdateManager
import com.kram.vlad.galleryapp.strategy.SortByDate
import com.kram.vlad.galleryapp.strategy.SortByLikes
import com.kram.vlad.galleryapp.strategy.Sorter

/**
 * Created by vlad on 11.03.2018.
 * App Utils class
 */
class Utils {
    companion object {
        val imagesUpdateManager = ImagesUpdateManager(Constants.ADD, Constants.UPDATE)
        private val sorter = Sorter()
        var images = ArrayList<ImageModel>()
        var from = 0
        var to = 10
        var sortMode = Constants.SORT_DATE_INCREASE

        var completedThreads = 0

        fun sortImages() {
            Utils.completedThreads = 0
            if (sortMode == Constants.SORT_DATE_DECREASE || sortMode == Constants.SORT_DATE_INCREASE) {
                sorter.strategy = SortByDate()
            } else {
                sorter.strategy = SortByLikes()
            }

            sorter.sort()
            imagesUpdateManager.notify(Constants.UPDATE)
        }


        fun ArrayList<ImageModel>.swap(j: Int) {
            val temp = this[j]
            this[j] = this[j + 1]
            this[j + 1] = temp
        }
    }
}