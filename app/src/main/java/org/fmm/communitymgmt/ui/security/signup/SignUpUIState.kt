package org.fmm.communitymgmt.ui.security.signup

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

sealed class SignUpUIState {
    data object Loading: SignUpUIState()
    data class Error(val error:String): SignUpUIState()
    data class EditMode(val userInfo: UserInfoModel, val person: PersonModel): SignUpUIState()

}