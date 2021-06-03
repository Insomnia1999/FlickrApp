package com.test.flickrapp.app

import android.app.Application
import com.test.flickrapp.modules.apiModule
import com.test.flickrapp.modules.repositoryModule
import com.test.flickrapp.modules.retrofitModule
import com.test.flickrapp.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(viewModelModule, retrofitModule, repositoryModule, apiModule)
        }
    }
}