package com.yasinsenel.yapacaklarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.repository.TodoRepositoryImp
import com.yasinsenel.yapacaklarm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val repository: TodoRepositoryImp) : ViewModel() {
    val getImageData = MutableLiveData<UnsplashModel?>()

    val getRoomList = MutableLiveData<MutableList<TodoData>?>()


    //Main Fragment
    fun getAllData(){
        viewModelScope.launch {
            val result = repository.gettAllData()
            getRoomList.value = result
        }
    }

    fun getWeatherData(categoryName : String){
        viewModelScope.launch {
            val result = repository.getImageResponse(categoryName)
            when(result){
                is Resource.Success->{
                    getImageData.value = result.data
                }
                is Resource.Loading ->{
                    println("yukleniyor")
                }
                is Resource.Error -> {
                    println("hata")
                }
            }
        }

    }
    // AddItemFragment
    fun addItem(todoData: TodoData){
        viewModelScope.launch {
            repository.insertItem(todoData)
        }
    }
    fun deleteItem(todoData: TodoData){
        viewModelScope.launch {
            repository.deleteItem(todoData)
        }
    }
    fun updateItem(todoData: TodoData){
        viewModelScope.launch {
            repository.updateItem(todoData)
        }
    }
}