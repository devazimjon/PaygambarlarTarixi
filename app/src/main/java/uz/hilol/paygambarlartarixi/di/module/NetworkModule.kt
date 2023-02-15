package uz.hilol.paygambarlartarixi.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.hilol.paygambarlartarixi.common.util.Constants.BASE_URL
import uz.hilol.paygambarlartarixi.di.annotation.AppScope

@Module
object NetworkModule {

    @Provides
    @AppScope
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @AppScope
    fun providesGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @AppScope
    fun provideGson(): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
}