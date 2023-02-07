package com.yasinsenel.yapacaklarm.model

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class TodoData(
    @ColumnInfo("todoName")
    var todoName : String = "",
    @ColumnInfo("todoDesc")
    var todoDesc : String = "",
    @ColumnInfo("todoDate")
    var todoDate : String = "",
    @ColumnInfo("todoTime")
    var todoTime : String = "",
    @ColumnInfo("todoImage")
    var todoImage : String = "",
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
) : Parcelable
