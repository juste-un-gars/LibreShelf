package com.libreshelf.di

import android.content.Context
import com.libreshelf.domain.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSmbClient(): SmbClient {
        return SmbClient()
    }

    @Provides
    @Singleton
    fun provideWebDavClient(): WebDavClient {
        return WebDavClient()
    }

    @Provides
    @Singleton
    fun provideFtpClient(): FtpClient {
        return FtpClient()
    }

    @Provides
    @Singleton
    fun provideNetworkManager(
        @ApplicationContext context: Context,
        smbClient: SmbClient,
        webDavClient: WebDavClient,
        ftpClient: FtpClient
    ): NetworkManager {
        return NetworkManager(context, smbClient, webDavClient, ftpClient)
    }
}
