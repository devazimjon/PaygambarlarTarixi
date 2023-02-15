package uz.hilol.paygambarlartarixi.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import uz.hilol.paygambarlartarixi.common.pref.SettingsStorage
import uz.hilol.paygambarlartarixi.data.database.AppDatabase
import uz.hilol.paygambarlartarixi.data.database.databasehelper.SqlDatabaseHelper
import uz.hilol.paygambarlartarixi.di.annotation.AppScope
import uz.hilol.paygambarlartarixi.di.module.AppModule
import uz.hilol.paygambarlartarixi.di.module.DatabaseModule
import uz.hilol.paygambarlartarixi.di.module.RepositoryModule
import uz.hilol.paygambarlartarixi.di.module.builder.ConnectivityBuilder
import uz.hilol.paygambarlartarixi.di.module.viewmodel.ViewModelFactoryModule
import uz.hilol.paygambarlartarixi.ui.base.MainActivity
import uz.hilol.paygambarlartarixi.ui.apps.AppsFragment
import uz.hilol.paygambarlartarixi.ui.dialog.NightModePickerDialog
import uz.hilol.paygambarlartarixi.ui.profile.ProfileFragment
import uz.hilol.paygambarlartarixi.ui.reader.ReaderFragment
import uz.hilol.paygambarlartarixi.ui.reader.singlepage.ReaderPageFragment
import uz.hilol.paygambarlartarixi.ui.reader.contentpicker.ContentPickerFragment
import uz.hilol.paygambarlartarixi.ui.reader.settings.SettingsBottomSheetDialog
import uz.hilol.paygambarlartarixi.ui.splash.SplashFragment

@AppScope
@Component(
    modules = [
        AppModule::class,
        ConnectivityBuilder::class,
        DatabaseModule::class,
//        InterceptorModule::class,
//        NetworkModule::class,
        RepositoryModule::class,
        ViewModelFactoryModule::class,
    ]
)
interface AppComponent {

    var settingsStorage: SettingsStorage
    var dbHelper: SqlDatabaseHelper
    var database: AppDatabase

    fun inject(app: Application)
    fun inject(app: SplashFragment)
    fun inject(act: MainActivity)
    fun inject(rtx: AppsFragment)
    fun inject(rtx: ProfileFragment)
    fun inject(nightDialog: NightModePickerDialog)
    fun inject(fr: ReaderFragment)
    fun inject(fr: ReaderPageFragment)
    fun inject(fr: ContentPickerFragment)
    fun inject(fr: SettingsBottomSheetDialog)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Application,
        ): AppComponent
    }

    companion object {

        @Volatile
        private var appComponent: AppComponent? = null

        fun get(): AppComponent =
            requireNotNull(appComponent) { "AppComponent is not initialized" }

        fun create(context: Application): AppComponent {
            appComponent?.let { return it }
            synchronized(this) {
                if (appComponent == null)
                    appComponent = DaggerAppComponent.factory().create(context)
            }
            return get()
        }
    }
}