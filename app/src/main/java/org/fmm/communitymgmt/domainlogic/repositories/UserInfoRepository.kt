package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

interface UserInfoRepository {
    suspend fun getUserInfo():UserInfoModel
}