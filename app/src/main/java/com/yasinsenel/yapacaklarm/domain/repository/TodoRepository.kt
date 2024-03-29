package com.yasinsenel.yapacaklarm.domain.repository

import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.yasinsenel.yapacaklarm.data.model.User
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.room.TodoDAO
import com.yasinsenel.yapacaklarm.service.ImagesAPI
import com.yasinsenel.yapacaklarm.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TodoRepository  {
    suspend fun getImageResponse(categoryName : String?) : UnsplashModel

    suspend fun getAllData(userId : String?) : MutableList<TodoData>

    suspend fun insertItem(todoData: TodoData)

    suspend fun deleteItem(todoData: TodoData)

    suspend fun updateItem(todoData: TodoData)

    suspend fun getDataFromFirestore(todoData: TodoData) : MutableList<TodoData>

    suspend fun saveDatatoFirestorage(todoData: TodoData)

    suspend fun saveTodoDatatoFirestore(todoData: TodoData)

    suspend fun getDataFromFireStorage(userId : String) : Uri?

    suspend fun saveUserDatatoFirestore(userId:String,name: String, email: String, password : String,activity : FragmentActivity)

    suspend fun getUserDataFromFirestore(view: View) : User

    suspend fun removeTodoDatatoFirestore(todoData: TodoData)

    suspend fun removeTodoDatatoStorage(todoData: TodoData)
}