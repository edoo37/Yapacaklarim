package com.yasinsenel.yapacaklarm.domain.usecase


import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.domain.repository.TodoRepository
import com.yasinsenel.yapacaklarm.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class InsertTodoDataToFirestore @Inject constructor(private val repository : TodoRepository) {
    suspend fun invoke(todoData: TodoData) = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.saveTodoDatatoFirestore(todoData)))
        }catch (t:Throwable){
            emit(Resource.Error(t.toString()))
        }
    }
}