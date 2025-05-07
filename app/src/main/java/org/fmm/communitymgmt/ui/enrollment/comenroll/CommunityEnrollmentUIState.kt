package org.fmm.communitymgmt.ui.enrollment.comenroll

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

sealed class CommunityEnrollmentUIState {
    data object Loading: CommunityEnrollmentUIState()
    data class Error(val error:String): CommunityEnrollmentUIState()
    data object EditMode: CommunityEnrollmentUIState()
    // @todo Crear un EditOtherMode -> para editar otro usuario (para superuser)
    data object RegisteredMode: CommunityEnrollmentUIState()
    data object MarriedMode: CommunityEnrollmentUIState()
    data object SingleMode: CommunityEnrollmentUIState()
}