package com.yasinsenel.yapacaklarm.domain.usecase


import com.yasinsenel.yapacaklarm.domain.repository.TodoRepository
import com.yasinsenel.yapacaklarm.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetDataFromFireStorageUsecase @Inject constructor(private val repository : TodoRepository) {
    suspend fun invoke(userId : String) = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getDataFromFireStorage(userId)))
        }catch (t:Throwable){
            emit(Resource.Error(t.toString()))
        }
    }
}