package com.yasinsenel.yapacaklarm.domain.usecase


import android.view.View
import com.yasinsenel.yapacaklarm.domain.repository.TodoRepository
import com.yasinsenel.yapacaklarm.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetUserDataFromFirestore @Inject constructor(private val repository : TodoRepository) {
    suspend fun invoke(view : View) = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getUserDataFromFirestore(view)))
        }catch (t:Throwable){
            emit(Resource.Error(t.toString()))
        }
    }
}