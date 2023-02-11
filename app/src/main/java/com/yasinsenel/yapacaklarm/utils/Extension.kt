package com.yasinsenel.yapacaklarm.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.yasinsenel.yapacaklarm.view.fragment.AddTaskFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Context.createWorkRequest(title: String,message:String, timeDelayInSeconds: Long, randomString:String) {
    val myWorkRequest = OneTimeWorkRequestBuilder<RemindWorker>()
        .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
        .setInputData(
            workDataOf(
                "title" to title,
                "message" to message,
            )
        ).addTag(randomString)
        .build()

    WorkManager.getInstance(this).enqueue(myWorkRequest)
}

fun Context.removeWorkReqeust(getString : String){
    WorkManager.getInstance(this).cancelAllWorkByTag(getString)
}

lateinit var currentPhotoPath: String
@Throws(IOException::class)
fun Activity?.createImageFile(): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = this?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = absolutePath
    }
}





fun Context.invokeCamera(activity : Activity){
    val file = activity.createImageFile()
    var uri : Uri? = null
    var getContent : ActivityResultLauncher<Uri>? = null
    try{
        uri = FileProvider.getUriForFile(this,"com.yasinsenel.yapacaklarm.fileprovider",file)
    }
    catch (e : Exception){

    }
    getContent?.launch(uri)
}
