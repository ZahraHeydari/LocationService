package com.android.tinysquare.di

import android.app.NotificationManager
import android.content.Context
import com.android.tinysquare.data.repository.UserRepositoryImp
import com.android.tinysquare.data.repository.VenueRepositoryImp
import com.android.tinysquare.data.source.local.UserLocalDataSource
import com.android.tinysquare.data.source.local.UserLocalDataSourceImp
import com.android.tinysquare.data.source.local.base.AppDatabase
import com.android.tinysquare.data.source.local.VenueLocalDataSource
import com.android.tinysquare.data.source.local.VenueLocalDataSourceImp
import com.android.tinysquare.data.source.remote.UserRemoteDataSource
import com.android.tinysquare.data.source.remote.UserRemoteDataSourceImp
import com.android.tinysquare.data.source.remote.VenueRemoteDataSource
import com.android.tinysquare.data.source.remote.VenueRemoteDataSourceImp
import com.android.tinysquare.data.source.remote.base.ApiService
import com.android.tinysquare.domain.repository.UserRepository
import com.android.tinysquare.domain.repository.VenueRepository
import com.android.tinysquare.domain.usecase.*
import com.android.tinysquare.presentation.NetworkStateBroadcastReceiver
import com.android.tinysquare.presentation.detail.DetailViewModel
import com.android.tinysquare.presentation.venues.VenuesFragment
import com.android.tinysquare.presentation.venues.VenuesViewModel
import com.google.android.gms.location.LocationRequest
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    scope(named<VenuesFragment>()){
       scoped { NetworkStateBroadcastReceiver() }
    }

    single {
        createNotificationManager(get())
    }

    single {
        LocationRequest()
    }

    single { createVenuesRepository(get(), get()) }

    single { createVenuesLocalDataSource(get()) }

    single { createVenuesRemoteDataSource(get()) }

    //Venues
    single { VenuesViewModel(get(), get(), get() ,get(), get()) }

    single { createGetVenuesUseCase(get()) }

    single { createGetVenuesFromDbUseCase(get()) }

    single { createInsertVenuesToDbUseCase(get()) }

    //Detail
    single { DetailViewModel(get()) }

    single { createGetVenueDetailUseCase(get()) }

    //User
    single { createUserRepository(get(), get()) }

    single { createUserLocalDataSource(get()) }

    single { createUserRemoteDataSource(get()) }

    single { createInsertUserLocationToDbUseCase(get()) }

    single { createGetUserLocationFromDbUseCase(get()) }
}

fun createNotificationManager(context: Context): NotificationManager {
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

fun createGetVenuesUseCase(venueRepository: VenueRepository): GetVenuesUseCase {
    return GetVenuesUseCase(venueRepository)
}

fun createGetVenuesFromDbUseCase(venueRepository: VenueRepository): GetVenuesFromDbUseCase {
    return GetVenuesFromDbUseCase(venueRepository)
}

fun createInsertVenuesToDbUseCase(venueRepository: VenueRepository): InsertVenuesToDbUseCase {
    return InsertVenuesToDbUseCase(venueRepository)
}

fun createGetVenueDetailUseCase(venueRepository: VenueRepository): GetVenueDetailUseCase {
    return GetVenueDetailUseCase(venueRepository)
}

fun createVenuesRepository(
    venueRemoteDataSource: VenueRemoteDataSource,
    venueLocalDataSource: VenueLocalDataSource
): VenueRepository {
    return VenueRepositoryImp(venueRemoteDataSource, venueLocalDataSource)
}

fun createVenuesLocalDataSource(appDatabase: AppDatabase): VenueLocalDataSource {
    return VenueLocalDataSourceImp(appDatabase)
}

fun createVenuesRemoteDataSource(apiService: ApiService): VenueRemoteDataSource {
    return VenueRemoteDataSourceImp(apiService)
}

fun createUserRepository(
    userRemoteDataSource: UserRemoteDataSource,
    userLocalDataSource: UserLocalDataSource
): UserRepository {
    return UserRepositoryImp(userRemoteDataSource, userLocalDataSource)
}

fun createUserLocalDataSource(appDatabase: AppDatabase): UserLocalDataSource {
    return UserLocalDataSourceImp(appDatabase)
}

fun createUserRemoteDataSource(apiService: ApiService): UserRemoteDataSource {
    return UserRemoteDataSourceImp(apiService)
}

fun createInsertUserLocationToDbUseCase(userRepository: UserRepository): InsertUserLocationToDbUseCase {
    return InsertUserLocationToDbUseCase(userRepository)
}

fun createGetUserLocationFromDbUseCase(userRepository: UserRepository): GetUserLocationFromDbUseCase {
    return GetUserLocationFromDbUseCase(userRepository)
}



