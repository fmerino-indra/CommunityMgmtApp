package org.fmm.communitymgmt.ui.common

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    userSession: UserSession
): ViewModel() {
    private val _userInfo = userSession.userInfo!!
    val userInfo: UserInfoModel get() = _userInfo
}