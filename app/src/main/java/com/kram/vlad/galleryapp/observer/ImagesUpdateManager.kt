package com.kram.vlad.galleryapp.observer

import android.util.Log


/**
 * Created by vlad on 18.03.2018.
 * Manage events for listeners
 */

class ImagesUpdateManager(vararg operations: String) {
    private var listeners: MutableMap<String, ArrayList<EventListener>> = HashMap()

    private val TAG = this::class.java.simpleName

    init {
        for (operation in operations) {
            this.listeners[operation] = ArrayList()
        }
    }

    fun subscribe(eventType: String, listener: EventListener) {
        val users = listeners[eventType]
        users!!.add(listener)
    }

    fun unsubscribe(eventType: String, listener: EventListener) {
        val users = listeners[eventType]
        val index = users!!.indexOf(listener)
        users.removeAt(index)
    }

    fun notify(eventType: String) {
        Log.d(TAG, listeners[eventType]!!.size.toString())
        val users = listeners[eventType]
        for (listener in users!!) {
            listener.update(eventType)
        }
    }
}