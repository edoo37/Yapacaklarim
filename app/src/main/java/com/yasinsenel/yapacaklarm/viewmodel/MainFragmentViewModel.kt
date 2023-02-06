package com.yasinsenel.yapacaklarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.repository.TodoRepository
import com.yasinsenel.yapacaklarm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {
    val getImageData = MutableLiveData<UnsplashModel?>()

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
}