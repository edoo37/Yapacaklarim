package com.yasinsenel.yapacaklarm.domain.usecase


import androidx.fragment.app.FragmentActivity
import com.yasinsenel.yapacaklarm.domain.repository.TodoRepository
import com.yasinsenel.yapacaklarm.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class InsertUserDataToFirestoreUsecase @Inject constructor(private val repository : TodoRepository) {
    suspend fun invoke(userId:String,name: String, email: String, password : String,activity : FragmentActivity) = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.saveUserDatatoFirestore(userId,name, email, password,activity)))
        }catch (t:Throwable){
            emit(Resource.Error(t.toString()))
        }
    }
}