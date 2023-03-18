package com.yasinsenel.yapacaklarm.service

import com.yasinsenel.yapacaklarm.model.UnsplashModel
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ImagesAPI {
    @Headers("Accept-Version: v1", "Authorization: Client-ID SX_0m163LKoefWyRCp6ZX1u0AYjlYkF6RyNaFL6OtSE")
    @GET("photos/random/")
    suspend fun getData(@Query("query") categoryName : String?) : UnsplashModel
}