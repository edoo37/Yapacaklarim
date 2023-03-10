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
class AddTaskFragmentViewModel @Inject constructor(
    private val insertTodoDataToStorage: InsertTodoDataToStorage,
    private val insertTodoUsecase: InsertTodoUsecase,
) : ViewModel() {


    // AddItemFragment
    fun addItem(todoData: TodoData){
        viewModelScope.launch {
            insertTodoUsecase.invoke(todoData).collect{
            }
        }
    }

    fun addTodoImageToFireStorage(todoData: TodoData){
        viewModelScope.launch {
            insertTodoDataToStorage.invoke(todoData).collect{

            }
        }
    }

}