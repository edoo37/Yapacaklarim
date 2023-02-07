package com.yasinsenel.yapacaklarm.repository

import androidx.lifecycle.MutableLiveData
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.room.TodoDAO
import com.yasinsenel.yapacaklarm.service.ImagesAPI
import com.yasinsenel.yapacaklarm.utils.Resource
import javax.inject.Inject

class TodoRepository @Inject constructor(private val api : ImagesAPI ,private val todoDAO: TodoDAO) {
    suspend fun getImageResponse(categoryName : String) : Resource<UnsplashModel>{
        val response = try {
            api.getData(categoryName)

        }catch (t : Throwable){
            return Resource.Error(t.message.toString())
        }
        return Resource.Success(response)
    }
    suspend fun gettAllData() : MutableList<TodoData> {
        val list = todoDAO.gettAllList()
        return list
    }
    suspend fun insertItem(todoData: TodoData){
        todoDAO.insertItem(todoData)
    }
    suspend fun deleteItem(todoData: TodoData){
        todoDAO.deleteItem(todoData)
    }
}