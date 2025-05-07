package org.fmm.communitymgmt.data.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.fmm.communitymgmt.BuildConfig.BASE_URL
import org.fmm.communitymgmt.data.core.interceptors.AuthInterceptor
import org.fmm.communitymgmt.data.network.response.AbstractRelationshipDTO
import org.fmm.communitymgmt.data.network.response.MarriageDTO
import org.fmm.communitymgmt.data.network.response.OtherDTO
import org.fmm.communitymgmt.data.network.response.SingleDTO
import org.fmm.communitymgmt.data.repositories.CommunityListRepositoryImpl
import org.fmm.communitymgmt.data.repositories.RelationshipRepositoryImpl
import org.fmm.communitymgmt.data.repositories.UserInfoRepositoryImpl
import org.fmm.communitymgmt.domainlogic.repositories.CommunityListRepository
import org.fmm.communitymgmt.domainlogic.repositories.RelationshipRepository
import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainlogic.usecase.SignUpUserInfoUseCase
import org.fmm.communitymgmt.ui.security.util.EncryptedPrefsStorage
import retrofit2.Retrofit
import javax.inject.Singleton


val relationshipModule = SerializersModule {
    polymorphic(AbstractRelationshipDTO::class) {
        subclass(MarriageDTO::class, MarriageDTO.serializer())
        subclass(SingleDTO::class, SingleDTO.serializer())
        subclass(OtherDTO::class, OtherDTO.serializer())
    }
}

val json = Json {
    serializersModule = relationshipModule
    classDiscriminator ="type"
    ignoreUnknownKeys = true
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //        .addConverterFactory()
    @Provides
    @Singleton
    @OptIn(ExperimentalSerializationApi::class) // Marcamos la función porque algunas
    // características están marcadas como experimentales.
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(interceptor)
            .build()
    }

    // CommunityList Api Service and Repository
    @Provides
    fun provideCommunityListApiService(retrofit:Retrofit):CommunityListApiService {
        return retrofit.create(CommunityListApiService::class.java)
    }
    @Provides
    fun provideCommunityListRepository(communityListApiService: CommunityListApiService)
    :CommunityListRepository {
        return CommunityListRepositoryImpl(communityListApiService)
    }

    // UserInfo Api Service and Repository
    @Provides
    fun provideUserInfoApiService(retrofit:Retrofit): UserInfoApiService {
        return retrofit.create(UserInfoApiService::class.java)
    }
    @Provides
    fun provideUserInfoRepository(userInfoApiService: UserInfoApiService)
            :UserInfoRepository {
        return UserInfoRepositoryImpl(userInfoApiService)
    }
    @Provides
    fun provideUserInfoUseCase(userInfoRepository: UserInfoRepository): SignUpUserInfoUseCase {
        return SignUpUserInfoUseCase(userInfoRepository)
    }
/*
    // Community Api Service
    @Provides
    fun provideEnrollCommunityApiService(retrofit: Retrofit):EnrollCommunityApiService {
        return retrofit.create(EnrollCommunityApiService::class.java)
    }
    @Provides
    fun provideEnrollCommunityRepository(enrollCommunityApiService: EnrollCommunityApiService):
    EnrollCommunityRepository {
        return EnrollCommunityRepositoryImpl(enrollCommunityApiService)
    }
    @Provides
    fun provideEnrollCommunityUseCase(enrollCommunityRepository: EnrollCommunityRepository)
    :EnrollCommunityUseCase {
        return EnrollCommunityUseCase(enrollCommunityRepository)
    }

 */

    // Relationship Api Service and Repository
    @Provides
    fun provideRelationshipApiService(retrofit:Retrofit):RelationshipApiService {
        return retrofit.create(RelationshipApiService::class.java)
    }
    @Provides
    fun provideRelationshipRepository(relationshipApiService: RelationshipApiService)
            :RelationshipRepository {
        return RelationshipRepositoryImpl(relationshipApiService)
    }

    // EncryptedPrefsStorage
    @Provides
    @Singleton
    fun provideEncryptedPrefsStorage (@ApplicationContext context: Context):
            EncryptedPrefsStorage {
        return EncryptedPrefsStorage(context)
    }
}