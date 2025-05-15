package com.synapse.joyers.di

import com.synapse.joyers.apiData.Repository
import org.koin.dsl.module


val repositoryModule = module {
    factory {  Repository(get()) }
}