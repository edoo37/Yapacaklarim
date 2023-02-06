package com.yasinsenel.yapacaklarm.repository

import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.service.ImagesAPI
import com.yasinsenel.yapacaklarm.utils.Resource
import javax.inject.Inject

class TodoRepository @Inject constructor(private val api : ImagesAPI) {
    suspend fun getImageResponse(categoryName : String) : Resource<UnsplashModel>{
        val response = try {
            api.getData(categoryName)
        }catch (t : Throwable){
            return Resource.Error(t.message.toString())
        }
        return Resource.Success(response)
    }
}