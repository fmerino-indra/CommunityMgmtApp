package org.fmm.communitymgmt.ui.common

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.common.EnrollmentStates

sealed class UserInfoState() {
    data object NotLoggedIn: UserInfoState()
    data class LoggedInState(val state:UserInfoModel): UserInfoState()
    data class Error(val errorMessage: String, val error:Exception): UserInfoState()
}