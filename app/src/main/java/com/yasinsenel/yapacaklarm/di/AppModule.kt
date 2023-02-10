package com.yasinsenel.yapacaklarm.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
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
    fun injectDao(database: TodoDatabase) = database.todoDao()

}