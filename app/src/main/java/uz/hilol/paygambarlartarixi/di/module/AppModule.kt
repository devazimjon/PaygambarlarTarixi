package uz.hilol.paygambarlartarixi.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import uz.hilol.paygambarlartarixi.di.annotation.AppScope

@Module
object AppModule {

    @AppScope
    @Provides
    fun bindContext(application: Application): Context = application
}