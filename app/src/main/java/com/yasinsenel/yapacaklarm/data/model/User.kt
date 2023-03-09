package com.yasinsenel.yapacaklarm.data.model

import android.os.Parcelable


@kotlinx.parcelize.Parcelize
data class User(

    var name : String? =null,
    var email : String? = null,
    var password : String? = null,
): Parcelable