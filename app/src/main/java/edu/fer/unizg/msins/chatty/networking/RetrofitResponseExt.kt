package edu.fer.unizg.msins.chatty.networking

import retrofit2.Response
import timber.log.Timber

fun <T> Response<T>.ifSuccessful(onSuccess: (T) -> (Unit)) {
    try {
        if (this.isSuccessful) {
            onSuccess(this.body()!!)
        } else {
            Timber.wtf("Response wasn't successful: $this")
        }
    } catch (e: Throwable) {
        Timber.wtf("Response successful ($this), but exception was thrown : $e")
    }
}