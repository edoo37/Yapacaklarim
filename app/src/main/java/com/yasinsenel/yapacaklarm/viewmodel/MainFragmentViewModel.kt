package com.yasinsenel.yapacaklarm.viewmodel

import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinsenel.yapacaklarm.data.model.User
import com.yasinsenel.yapacaklarm.domain.usecase.*
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.domain.repository.TodoRepository
import com.yasinsenel.yapacaklarm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val getTodoUsecase: GetTodoUsecase,
    private val insertTodoUsecase: InsertTodoUsecase,
    private val updateTodoUsecase: UpdateTodoUsecase,
    private val deleteTodoUsecase: DeleteTodoUsecase,
    private val getApiDataUsecase: GetApiDataUsecase,
    private val insertUserDataToFirestore: InsertUserDataToFirestore,
    private val getUserDataFromFirestore: GetUserDataFromFirestore,
    private val getAllDataFromFirestore: GetAllDataFromFirestore,
    private val removeDataFromFirestore: RemoveDataFromFirestore,
    private val insertTodoDataToFirestore: InsertTodoDataToFirestore,
    private val getDataFromFireStorage: GetDataFromFireStorage
) : ViewModel() {



    val _getImageData = MutableStateFlow<Resource<UnsplashModel>?>(null)
    val getImageData : StateFlow<Resource<UnsplashModel>?> = _getImageData

    val _getRoomList = MutableStateFlow<Resource<MutableList<TodoData>>?>(null)
    val getRoomList : StateFlow<Resource<MutableList<TodoData>>?> = _getRoomList

    val _getAllTodoDataFromFirestore = MutableSharedFlow<Resource<MutableList<TodoData>>?>(replay=0)
    val getAllTodoDataFromFirestore : SharedFlow<Resource<MutableList<TodoData>>?> = _getAllTodoDataFromFirestore

    val _getUserDataFromFirestoree = MutableStateFlow<Resource<User>?>(null)
    val getUserDataFromFirestoree : StateFlow<Resource<User>?> = _getUserDataFromFirestoree

    val _getUserImageDataFromFirestore = MutableStateFlow<Resource<Uri>?>(null)
    val getUserImageDataFromFirestore : StateFlow<Resource<Uri>?> = _getUserImageDataFromFirestore

    val getFirebaseList = MutableLiveData<MutableList<TodoData>?>()



    //Main Fragment
    fun getAllData(userId : String){
        viewModelScope.launch(Dispatchers.IO){
            getTodoUsecase.invoke(userId).collect{
                _getRoomList.value = it
            }
        }
    }

    fun getApiData(categoryName : String){
        viewModelScope.launch {
            viewModelScope.launch(Dispatchers.IO){
                getApiDataUsecase.invoke(categoryName).collect{
                    _getImageData.value = it
                }
            }
        }

    }
    // AddItemFragment
    fun addItem(todoData: TodoData){
        viewModelScope.launch {
            insertTodoUsecase.invoke(todoData).collect{
            }
        }
    }
    fun deleteItem(todoData: TodoData){
        viewModelScope.launch {
            deleteTodoUsecase.invoke(todoData).collect{
            }
        }
    }
    fun updateItem(todoData: TodoData){
        viewModelScope.launch {
            updateTodoUsecase.invoke(todoData).collect{
            }
        }
    }
    fun saveUserDatatoFirestore(userId:String,name: String, email: String, password : String,activity : FragmentActivity){
        viewModelScope.launch {
            insertUserDataToFirestore.invoke(userId,name,email,password,activity).collect{

            }

        }
    }

    fun saveTodoDatatoFirestore(todoData: TodoData){
        viewModelScope.launch {
            insertTodoDataToFirestore.invoke(todoData).collect{

            }

        }
    }

    fun getUserDataFromFirestore(view : View){
        viewModelScope.launch(Dispatchers.IO) {
            getUserDataFromFirestore.invoke(view).collect{
                _getUserDataFromFirestoree.value = it
            }
        }
    }
    fun getDataFromFireStorage(userId : String){
        viewModelScope.launch(Dispatchers.IO) {
            getDataFromFireStorage.invoke(userId).collect{
                _getUserImageDataFromFirestore.value = it
            }
        }

    }
    fun removeDataFirestore(todoData: TodoData){
        viewModelScope.launch {
            removeDataFromFirestore.invoke(todoData).collect{

            }
        }
    }
    fun getAllTodoDataFromFirestore(todoData: TodoData){
        viewModelScope.launch {
            getAllDataFromFirestore.invoke(todoData).collect{
                _getAllTodoDataFromFirestore.emit(it)
            }
        }
    }
}