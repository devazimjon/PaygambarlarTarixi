package uz.hilol.paygambarlartarixi.di.module.builder

import uz.hilol.paygambarlartarixi.common.util.Connectivity
import uz.hilol.paygambarlartarixi.common.util.ConnectivityImpl
import dagger.Binds
import dagger.Module


@Module
abstract class ConnectivityBuilder {

    @Binds
    abstract fun bindConnectivity(connectivityImpl: ConnectivityImpl): Connectivity
}