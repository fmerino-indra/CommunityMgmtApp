package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject

class GetUserInfo @Inject constructor(private val userInfoRepository:UserInfoRepository) {
    suspend operator fun invoke(): UserInfoModel {
        return userInfoRepository.getUserInfo()
    }
}