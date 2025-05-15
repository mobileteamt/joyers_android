package com.synapse.joyers.di

import com.synapse.joyers.apiData.RemoteDataSource
import org.koin.dsl.module

val remoteDataSourceModule= module {
    factory {  RemoteDataSource(get()) }
}