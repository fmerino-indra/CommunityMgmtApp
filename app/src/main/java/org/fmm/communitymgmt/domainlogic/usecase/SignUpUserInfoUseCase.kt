package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject

class SignUpUserInfoUseCase @Inject constructor (private val repository: UserInfoRepository){
    suspend operator fun invoke(userInfo: UserInfoModel): UserInfoModel {
        return repository.signUpUserInfo(userInfo)
    }
}