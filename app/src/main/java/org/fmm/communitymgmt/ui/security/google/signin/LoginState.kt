package org.fmm.communitymgmt.ui.security.google.signin

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

sealed class LoginState {
    data object NotLoggedIn : LoginState()
    data class LoggingInState(val idToken: String): LoginState()
    data class LoggedInState(val idToken: String, val userInfo: UserInfoModel): LoginState()
    data class NotRegisteredState(val idToken: String, val userInfo: UserInfoModel): LoginState()
    data class Error(val errorMessage: String, val error:Exception): LoginState()
}