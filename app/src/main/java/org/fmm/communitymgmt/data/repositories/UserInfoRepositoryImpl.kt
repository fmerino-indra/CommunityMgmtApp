package org.fmm.communitymgmt.data.repositories

import android.util.Log
import org.fmm.communitymgmt.data.network.response.UserInfoApiService
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
        }
        throw RuntimeException("Problems receiving UserInfo")
    }
}