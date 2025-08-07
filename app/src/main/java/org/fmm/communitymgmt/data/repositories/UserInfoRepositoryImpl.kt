package org.fmm.communitymgmt.data.repositories

import android.util.Log
import org.fmm.communitymgmt.data.network.UserInfoApiService
import org.fmm.communitymgmt.data.network.response.UserInfoDTO
import org.fmm.communitymgmt.domainlogic.exceptions.SocialUserNotFoundException
import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(private val apiService: UserInfoApiService):
    UserInfoRepository {
    override suspend fun getUserInfo(): UserInfoModel {
        runCatching {
            apiService.geUserInfo()
        }.onSuccess {
            return it.toDomain()
        }.onFailure {
            Log.e("UserInfoRepositoryImpl", "Problems requesting UserInfo", it)
// Queda por re
            if (it is ConnectException) {
                throw RuntimeException(
                    "An exception has ocurred while trying to connect to " +
                            "server", it
                )
            } else if (it is HttpException && it.code() == 404) {
                throw SocialUserNotFoundException(
                    "The user: XXXX, has not been found",
                    it
                )
            } else {
                throw it
            }
        }
        throw RuntimeException("Unspected result quering UserInfo")
    }

    override suspend fun signUpUserInfo(userInfo: UserInfoModel): UserInfoModel {
        runCatching {
            apiService.postUserInfo(UserInfoDTO.fromDomain(userInfo))
        }.onSuccess {
            return it.toDomain()
        }.onFailure {
            Log.e("UserInfoRepositoryImpl", "Problems while posting UserInfo", it)
            if (it is ConnectException || (it is HttpException && it.code() == 500)) {
                throw RuntimeException(
                    "An exception has occurred while trying to connect to " +
                            "server", it
                )
//            } else if (it is HttpException && it.code() == 500) {
//                throw SocialUserNotFoundException(
//                    "The user: XXXX, has not been found",
//                    it
//                )
            } else {
                throw it
            }
        }
        throw RuntimeException("Exception while posting UserInfo")
    }

    override suspend fun signUpCommunity(userInfo: UserInfoModel): UserInfoModel {
        runCatching {
            apiService.putUserInfo(UserInfoDTO.fromDomain(userInfo))
        }.onSuccess {
            return it.toDomain()
        }.onFailure {
            Log.e("UserInfoRepositoryImpl", "Problems while posting UserInfo", it)
            if (it is ConnectException || (it is HttpException && it.code() == 500)) {
                throw RuntimeException(
                    "An exception has occurred while trying to connect to " +
                            "server", it
                )
//            } else if (it is HttpException && it.code() == 500) {
//                throw SocialUserNotFoundException(
//                    "The user: XXXX, has not been found",
//                    it
//                )
            } else {
                throw it
            }
        }
        throw RuntimeException("Exception while posting UserInfo")
    }
}