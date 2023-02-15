package uz.hilol.paygambarlartarixi.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import uz.hilol.paygambarlartarixi.di.annotation.AppScope
import uz.hilol.paygambarlartarixi.data.remote.AppService

@Module
class RepositoryModule {

    @Provides
    @AppScope
    fun provideService(retrofit: Retrofit): AppService {
        return retrofit.create(AppService::class.java)
    }
}