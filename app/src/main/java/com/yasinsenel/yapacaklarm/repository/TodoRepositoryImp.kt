package com.yasinsenel.yapacaklarm.repository

import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.room.TodoDAO
import com.yasinsenel.yapacaklarm.service.ImagesAPI
import com.yasinsenel.yapacaklarm.utils.Resource
import javax.inject.Inject

class TodoRepositoryImp @Inject constructor(private val api : ImagesAPI ,private val todoDAO: TodoDAO) : TodoRepository   {
    override suspend fun getImageResponse(categoryName: String): Resource<UnsplashModel> {
        val response = try {
            api.getData(categoryName)

        }catch (t : Throwable){
            return Resource.Error(t.message.toString())
        }
        return Resource.Success(response)
    }

    override suspend fun gettAllData(userId : String): MutableList<TodoData> {
        val list = todoDAO.gettAllList(userId)
        return list
    }

    override suspend fun insertItem(todoData: TodoData) {
        todoDAO.insertItem(todoData)
    }

    override suspend fun deleteItem(todoData: TodoData) {
        todoDAO.deleteItem(todoData)
    }

    override suspend fun updateItem(todoData: TodoData) {
        todoDAO.updateItem(todoData)
    }

}