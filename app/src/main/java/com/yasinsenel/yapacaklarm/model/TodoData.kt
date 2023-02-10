package com.yasinsenel.yapacaklarm.model

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
@Entity
data class TodoData(
    @ColumnInfo("todoName")
    var todoName : String? = null,
    @ColumnInfo("todoDesc")
    var todoDesc : String? = null,
    @ColumnInfo("todoDate")
    var todoDate : String? = null,
    @ColumnInfo("todoTime")
    var todoTime : String? = null,
    @ColumnInfo("todoImage")
    var todoImage : String? = null,
    @ColumnInfo("randomString")
    var randomString : String? = null,
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
) : Parcelable
