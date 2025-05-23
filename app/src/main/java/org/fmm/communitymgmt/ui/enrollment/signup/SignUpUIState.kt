package org.fmm.communitymgmt.ui.enrollment.signup

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

sealed class SignUpUIState {
    data object Loading: SignUpUIState()
    data class Error(val errorMessage:String, val error:Throwable): SignUpUIState()
    data object EditMode: SignUpUIState()
    // @todo Crear un EditOtherMode -> para editar otro usuario (para superuser)
    data object RegisteredMode: SignUpUIState()

}