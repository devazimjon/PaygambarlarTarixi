package uz.hilol.paygambarlartarixi.di.module

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import uz.hilol.paygambarlartarixi.di.annotation.AppScope
import java.io.File
import java.util.concurrent.TimeUnit

@Module
object InterceptorModule {

    @Provides
    @AppScope
    internal fun okHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        cache: Cache,
        context: Context
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool())
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(ChuckInterceptor(context).apply { showNotification(false) }) //TODO: delete in prod
            .cache(cache)
            .build()

    @Provides
    @AppScope
    internal fun loggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @AppScope
    fun provideCache(file: File): Cache {
        return Cache(file, 1000 * 1000 * 1000) //1000mb
    }

    @Provides
    @AppScope
    fun provideFile(context: Context): File {
        val file = File(context.cacheDir, "apiCache")
        file.mkdir()
        return file
    }
}