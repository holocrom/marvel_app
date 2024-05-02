package com.example.data.di

import com.example.data.ApiClient
import com.example.data.datasource.ConnectionDataSource
import com.example.data.datasource.ConnectionToApiDataSource
import com.example.data.datasource.CacheDataSource
import com.example.data.datasource.CacheDataSourceImpl
import com.example.data.ImpleApiClient
import com.example.data.repository.Repository
import com.example.domain.DomainLayerContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataInterfaceModule {

    @Binds
    @Singleton
    abstract fun  bindConnectionDataSource(bridge: ConnectionDataSource): ConnectionToApiDataSource

    @Binds
    abstract fun  bindRepository(bridge: Repository): DomainLayerContract

    @Binds
    abstract fun bindRetrofit(impleApiClient: ImpleApiClient): ApiClient

    @Binds
    @Singleton
    abstract fun bindPersistanceDataSource(bridge: CacheDataSourceImpl): CacheDataSource
}