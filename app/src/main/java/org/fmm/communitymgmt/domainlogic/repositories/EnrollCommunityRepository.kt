package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

@Deprecated("Se va a eliminar")
interface EnrollCommunityRepository {
    suspend fun enrollCommunity(userInfo: UserInfoModel):UserInfoModel
}