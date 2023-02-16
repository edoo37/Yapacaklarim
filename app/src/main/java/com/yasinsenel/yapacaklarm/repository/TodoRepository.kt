package com.yasinsenel.yapacaklarm.repository

import androidx.lifecycle.MutableLiveData
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.room.TodoDAO
import com.yasinsenel.yapacaklarm.service.ImagesAPI
import com.yasinsenel.yapacaklarm.utils.Resource
import javax.inject.Inject

interface TodoRepository  {
    suspend fun getImageResponse(categoryName : String) : Resource<UnsplashModel>

    suspend fun gettAllData(userId : String) : MutableList<TodoData>

    suspend fun insertItem(todoData: TodoData)

    suspend fun deleteItem(todoData: TodoData)

    suspend fun updateItem(todoData: TodoData)

}