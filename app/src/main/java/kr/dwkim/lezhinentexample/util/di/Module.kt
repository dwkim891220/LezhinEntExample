package kr.dwkim.lezhinentexample.util.di

import com.google.gson.GsonBuilder
import kr.dwkim.lezhinentexample.model.RepositoryImpl
import kr.dwkim.lezhinentexample.repository.ENDPOINT
import kr.dwkim.lezhinentexample.repository.IRepository
import kr.dwkim.lezhinentexample.repository.NetworkRepository
import kr.dwkim.lezhinentexample.util.rx.ApplicationSchedulerProvider
import kr.dwkim.lezhinentexample.util.rx.SchedulerProvider
import kr.dwkim.lezhinentexample.viewmodel.KakaoViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module{
    single {
        Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(
                OkHttpClient.Builder().apply{
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.HEADERS
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    addInterceptor{ chain ->
                        chain.proceed(
                            chain.request().newBuilder()
                                .header("Authorization", "KakaoAK 9cda93dfbfa9f878e61f4559f4f86a91")
                                .build()
                        )
                    }
                }.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(NetworkRepository::class.java)
    }

    single {
        RepositoryImpl(androidContext(), get()) as IRepository
    }
}

val viewModelModules = module {
    viewModel { KakaoViewModel(get(), get()) }
}

val rxModule = module {
    single<SchedulerProvider> { ApplicationSchedulerProvider() }
}

val appModules = listOf(apiModule, viewModelModules, rxModule)