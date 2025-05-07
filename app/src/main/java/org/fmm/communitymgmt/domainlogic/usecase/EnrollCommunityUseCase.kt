package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.EnrollCommunityRepository
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject

@Deprecated("Se va a eliminar")
class EnrollCommunityUseCase @Inject constructor (private val repository:
                                                      EnrollCommunityRepository){
    suspend operator fun invoke(userInfo: UserInfoModel): UserInfoModel {
        return repository.enrollCommunity(userInfo)
    }
}
