package org.fmm.communitymgmt.ui.security.google.signin

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

sealed class SignInState {
    data object NotLoggedIn : SignInState()
    data class LoggingInState(val idToken: String): SignInState()
    data class LoggedInState(val idToken: String, val userInfo: UserInfoModel): SignInState()
    data class NotActivatedState(val idToken: String, val userInfo: UserInfoModel): SignInState()
    data class NotRegisteredState(val idToken: String, val userInfo: UserInfoModel): SignInState()
    data class RegisteringState(val idToken: String, val userInfo: UserInfoModel): SignInState()
    data object NotCredentialsState: SignInState()
    data class Error(val errorMessage: String, val error:Exception): SignInState()
}