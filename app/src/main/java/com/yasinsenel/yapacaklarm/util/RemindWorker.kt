package com.yasinsenel.yapacaklarm.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yasinsenel.yapacaklarm.adapter.TodoAdapter

class RemindWorker(val context : Context,params : WorkerParameters) : Worker(context,params) {
    override fun doWork(): Result {
        return try {
            showData()
            Result.success()
        } catch (throwable: Throwable) {
            println(throwable)
            Result.failure()
        }
    }

    private fun showData(){
       NotificationHelper(context).createNotification(
            inputData.getString("title").toString(),
            inputData.getString("message").toString()
        )
        println("Deneme")
    }
}