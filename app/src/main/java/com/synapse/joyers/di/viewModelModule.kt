package com.synapse.joyers.di

import com.synapse.joyers.ui.loginforgot.LoginViewModel
import com.synapse.joyers.ui.signup.SignupViewModel
import com.synapse.joyers.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SignupViewModel(get(), get()) }
}

