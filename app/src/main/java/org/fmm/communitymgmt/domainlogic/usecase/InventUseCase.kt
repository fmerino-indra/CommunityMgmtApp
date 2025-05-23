package org.fmm.communitymgmt.domainlogic.usecase

import android.util.Log
import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.enrollment.brothers.ScanUseCase
import javax.inject.Inject

class InventUseCase @Inject constructor (private val invitationRespository:
                                                   InvitationRepository): ScanUseCase {
    override suspend fun executeAfterScan(code: String) {
        Log.d("InventUseCase", "Ha funcionado sin parámetros")
    }
}