package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

interface UserInfoRepository {
    suspend fun getUserInfo():UserInfoModel
    suspend fun signUpUserInfo(userInfo: UserInfoModel):UserInfoModel

    suspend fun signUpCommunity(userInfo: UserInfoModel): UserInfoModel
}