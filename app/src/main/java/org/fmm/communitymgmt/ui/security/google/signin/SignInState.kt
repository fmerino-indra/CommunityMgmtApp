package org.fmm.communitymgmt.ui.security.google.signin

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.common.EnrollmentStates

sealed class SignInState(val enrollmentState:EnrollmentStates?=null) {
    data object NotLoggedIn: SignInState()
    data object LoggingInState: SignInState()

    data object NotRegisteredState: SignInState(EnrollmentStates.NOTENROLLED)
    data object RegisteringState:  SignInState(EnrollmentStates.NOTCOMMUNITY)
    data object NotActivatedState: SignInState(EnrollmentStates.FULLENROLLED)

    // Full registered (a home)
    data class LoggedInState(val userInfo:UserInfoModel): SignInState(EnrollmentStates.ACTIVATED)

    data object NotCredentialsState: SignInState()
    data class Error(val errorMessage: String, val error:Exception): SignInState()
}