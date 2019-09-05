package com.example.spacexinfo.viewModels

open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getEventOrNullIfHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}