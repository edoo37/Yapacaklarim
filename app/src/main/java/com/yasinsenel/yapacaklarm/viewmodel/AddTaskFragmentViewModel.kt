package com.yasinsenel.yapacaklarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinsenel.yapacaklarm.domain.usecase.*
import com.yasinsenel.yapacaklarm.model.TodoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskFragmentViewModel @Inject constructor(
    private val insertTodoDataToStorage: InsertTodoDataToStorageUsecase,
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