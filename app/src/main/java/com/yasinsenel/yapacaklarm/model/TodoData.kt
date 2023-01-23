package com.yasinsenel.yapacaklarm.model

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TodoData(
    var todoName : String = "",
    var todoDesc : String = "",
    var todoDate : String = "",
    var todoTime : String = "",
    var todoImage : String = ""
) : Parcelable
