package com.yasinsenel.yapacaklarm.di

import com.yasinsenel.yapacaklarm.service.ImagesAPI
import com.yasinsenel.yapacaklarm.utils.Constans
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

}