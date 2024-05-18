package com.okladnikov.yadroweather.di

import com.okladnikov.yadroweather.BuildConfig
import com.okladnikov.yadroweather.home.network.LocationService
import com.okladnikov.yadroweather.home.network.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptorBody = HttpLoggingInterceptor()
        loggingInterceptorBody.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptorBody)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(okHttpClient: OkHttpClient): WeatherService {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.OPEN_METEO_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationService(okHttpClient: OkHttpClient): LocationService {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BIGDATACLOUD_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(LocationService::class.java)
    }
}
