package com.joesemper.goalmanager.di

import com.joesemper.goalmanager.data.GoalsRepository
import com.joesemper.goalmanager.data.MainRepository
import com.joesemper.goalmanager.data.db.DataProvider
import com.joesemper.goalmanager.data.db.FireStoreDatabaseProvider
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.presentation.GoalViewModel
import com.joesemper.goalmanager.presentation.MainViewModel
import com.joesemper.goalmanager.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

object DependencyGraph {

    private val repositoryModule by lazy {
        module {
            single { MainRepository(get()) } bind GoalsRepository::class
            single { FireStoreDatabaseProvider() } bind DataProvider::class
        }
    }

    private val viewModelModule by lazy {
        module {
            viewModel { MainViewModel(get()) }
            viewModel { SplashViewModel(get()) }
            viewModel { (goal: Goal?) -> GoalViewModel(get(), goal) }
        }
    }

    val modules: List<Module> = listOf(repositoryModule, viewModelModule)


}