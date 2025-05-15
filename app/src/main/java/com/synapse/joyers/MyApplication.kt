package com.synapse.joyers

import android.app.Application
import com.synapse.joyers.di.dataStoreModule
import com.synapse.joyers.di.networkModule
import com.synapse.joyers.di.remoteDataSourceModule
import com.synapse.joyers.di.repositoryModule
import com.synapse.joyers.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            androidLogger()
            modules(
                networkModule,
                remoteDataSourceModule,
                repositoryModule,
                viewModelModule,
                dataStoreModule
            )
        }
    }
}