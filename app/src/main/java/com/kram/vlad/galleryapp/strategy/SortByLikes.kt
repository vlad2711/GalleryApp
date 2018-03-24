package com.kram.vlad.galleryapp.strategy

import com.kram.vlad.galleryapp.Constants
import com.kram.vlad.galleryapp.utils.Utils
import com.kram.vlad.galleryapp.utils.Utils.Companion.swap

/**
 * Created by vlad on 24.03.2018.
 * Sort images by likes
 */
class SortByLikes : Strategy {
    override fun sort() {
        for (i in 0 until Utils.images.size - 1) {
            for (j in 0 until Utils.images.size - i - 1) {
                if ((Utils.images[j].likes < Utils.images[j + 1].likes) == (Utils.sortMode != Constants.SORT_LIKE_DECREASE)) {
                    Utils.images.swap(j)
                } else {
                    if (Utils.images[j].dateOfLastModified == Utils.images[j + 1].dateOfLastModified) Utils.images.swap(j)
                }
            }
        }
    }
}