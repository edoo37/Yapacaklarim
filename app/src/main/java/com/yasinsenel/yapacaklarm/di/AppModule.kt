package com.yasinsenel.yapacaklarm.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.yasinsenel.yapacaklarm.domain.repository.TodoRepository
import com.yasinsenel.yapacaklarm.data.repository.TodoRepositoryImp
import com.yasinsenel.yapacaklarm.room.TodoDAO
import com.yasinsenel.yapacaklarm.room.TodoDatabase
import com.yasinsenel.yapacaklarm.service.ImagesAPI
import com.yasinsenel.yapacaklarm.utils.Constans
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun injectRetrofit() : ImagesAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constans.BASE_URL)
            .build()
            .create(ImagesAPI::class.java)
    }
    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,TodoDatabase::class.java,"tododatabase"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideFirestoreRepository(
       api : ImagesAPI,
       todoDAO: TodoDAO,
       firebaseFirestore: FirebaseFirestore,
       auth: FirebaseAuth,
       firebaseStorage : StorageReference,
       application: Application
    ): TodoRepository = TodoRepositoryImp(api,todoDAO,firebaseFirestore,auth,firebaseStorage,application)

    @Singleton
    @Provides
    fun injectDao(database: TodoDatabase) = database.todoDao()

    @Singleton
    @Provides
    fun provideFirebaseFirestoreInstance() : FirebaseFirestore{
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageInstance(): StorageReference {
        return Firebase.storage.reference
    }

    @Singleton
    @Provides
    fun provideFirebaseDatabaseInstance() : FirebaseDatabase{
        return Firebase.database
    }

}