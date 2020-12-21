package com.joesemper.goalmanager

import androidx.multidex.MultiDexApplication
import com.joesemper.goalmanager.di.DependencyGraph
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GoalsApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GoalsApp)
            modules(DependencyGraph.modules)
        }
    }
}