package edu.fer.unizg.msins.chatty.networking

import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors

//Replace with coroutines later, this works well but we already have plenty of threads on Dispatchers.IO to use
object DownloadScheduler {
    val queue = ArrayBlockingQueue<Runnable>(200)

    private val scheduler = Executors.newSingleThreadExecutor()
    private val executor = Executors.newFixedThreadPool(4)

    fun start() {
        scheduler.execute {
            while (true) {
                try {
                    executor.execute(queue.take())
                } catch (e: InterruptedException) {
                    break
                } catch (logged: Exception){
                    Timber.wtf("Failed to download: ${logged.stackTrace}")
                }
            }
        }
    }
}