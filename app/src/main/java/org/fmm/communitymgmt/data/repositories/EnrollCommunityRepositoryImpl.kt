package org.fmm.communitymgmt.data.repositories

import android.util.Log
import org.fmm.communitymgmt.data.network.EnrollCommunityApiService
import org.fmm.communitymgmt.data.network.response.UserInfoDTO
import org.fmm.communitymgmt.domainlogic.repositories.EnrollCommunityRepository
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject

@Deprecated("Se va a eliminar")
class EnrollCommunityRepositoryImpl @Inject constructor(
    private val apiService: EnrollCommunityApiService
): EnrollCommunityRepository {
    override suspend fun enrollCommunity(userInfo: UserInfoModel): UserInfoModel {
        runCatching {
            apiService.enrollCommunity(UserInfoDTO.fromDomain(userInfo))
        }.onSuccess {
            return it.toDomain()
        }.onFailure {
            Log.e("EnrollCommunityRepositoryImpl", "Problems while posting Community", it)
        }
        throw RuntimeException("Exception while posting Community")
    }
}