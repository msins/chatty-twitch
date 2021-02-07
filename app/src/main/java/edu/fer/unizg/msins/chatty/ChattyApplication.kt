package edu.fer.unizg.msins.chatty

import android.app.Application
import edu.fer.unizg.msins.chatty.networking.DownloadScheduler
import timber.log.Timber

class ChattyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        DownloadScheduler.start()
    }
}