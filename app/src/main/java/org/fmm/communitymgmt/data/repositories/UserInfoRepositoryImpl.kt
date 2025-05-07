package org.fmm.communitymgmt.data.repositories

import android.util.Log
import org.fmm.communitymgmt.data.network.UserInfoApiService
import org.fmm.communitymgmt.data.network.response.UserInfoDTO
import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
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
            throw RuntimeException("Problems receiving UserInfo", it)
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
        }
        throw RuntimeException("Exception while posting UserInfo")
    }
}