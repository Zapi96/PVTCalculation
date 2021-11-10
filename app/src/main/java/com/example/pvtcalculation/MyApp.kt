package com.example.pvtcalculation

import android.app.Application
import com.example.pvtcalculation.positions.data.PositionDataSource
import com.example.pvtcalculation.positions.data.local.LocalDB
import com.example.pvtcalculation.positions.data.local.PositionsLocalRepository
import com.example.pvtcalculation.positions.positionslist.ListPositionsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                ListPositionsViewModel(
                    get(),
                    get() as PositionDataSource
                )
            }
            //Declare singleton definitions to be later injected using by inject()
            single {
                //This view model is declared singleton to be used across multiple fragments
                ConfigurationDownloadViewModel(
                    get(),
                    get() as PositionDataSource
                )
            }
            single { PositionsLocalRepository(get()) as PositionDataSource }
            single { LocalDB.createPositionsDao(this@MyApp) }
        }

        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}