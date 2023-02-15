package uz.hilol.paygambarlartarixi.di.module.viewmodel

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import uz.hilol.paygambarlartarixi.di.module.viewmodel.factory.ViewModelKey
import uz.hilol.paygambarlartarixi.ui.base.MainActivityViewModel
import uz.hilol.paygambarlartarixi.ui.apps.AppsViewModel
import uz.hilol.paygambarlartarixi.ui.reader.ReaderFragmentVM
import uz.hilol.paygambarlartarixi.ui.reader.contentpicker.ContentPickerFragmentVM
import uz.hilol.paygambarlartarixi.ui.reader.settings.SettingsBottomSheetVM

@Module
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainActivityViewModel(mainViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppsViewModel::class)
    fun bindAppsViewModel(viewModel: AppsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsBottomSheetVM::class)
    fun bindSettingBottomSheetViewModel(viewModel: SettingsBottomSheetVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReaderFragmentVM::class)
    fun bindReaderViewModel(viewModel: ReaderFragmentVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContentPickerFragmentVM::class)
    fun bindContentPickerViewModel(viewModel: ContentPickerFragmentVM): ViewModel
}