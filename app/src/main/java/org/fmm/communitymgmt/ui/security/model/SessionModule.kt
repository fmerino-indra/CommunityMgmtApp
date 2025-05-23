package org.fmm.communitymgmt.ui.security.model

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
object SessionModule {
//    @Provides
//    @Singleton
    fun provideUserSession(): UserSession {
        return UserSession()
    }
}