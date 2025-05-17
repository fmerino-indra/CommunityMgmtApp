package org.fmm.communitymgmt.ui.security.google.signin

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.common.EnrollmentStates

sealed class SignInState(val state:EnrollmentStates?=null) {
    data object NotLoggedIn: SignInState()
    data object LoggingInState: SignInState()

    data object NotRegisteredState: SignInState(EnrollmentStates.NOTENROLLED)
    data object RegisteringState:  SignInState(EnrollmentStates.NOTCOMMUNITY)
    data object NotActivatedState: SignInState(EnrollmentStates.FULLENROLLED)
    //data class LoggingInState(val idToken: String): SignInState()
    // Full registered (a home)
    data class LoggedInState(val userInfo:UserInfoModel): SignInState(EnrollmentStates.ACTIVATED)

    // person==null (OwnEnrollment)
    //data class NotRegisteredState(val idToken: String, val userInfo: UserInfoModel): SignInState()
    // person!=null && community==null (OwnEnrollment)
    //data class RegisteringState(val idToken: String, val userInfo: UserInfoModel): SignInState()
    // person!=null && community!=null && !activated (OwnEnrollment)
    //data class NotActivatedState(val idToken: String, val userInfo: UserInfoModel): SignInState()

    data object NotCredentialsState: SignInState()
    data class Error(val errorMessage: String, val error:Exception): SignInState()
}