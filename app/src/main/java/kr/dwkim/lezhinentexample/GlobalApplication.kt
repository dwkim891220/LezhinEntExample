package kr.dwkim.lezhinentexample

import android.app.Application
import kr.dwkim.lezhinentexample.util.di.appModules
import org.koin.android.ext.android.startKoin

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, appModules)
    }
}