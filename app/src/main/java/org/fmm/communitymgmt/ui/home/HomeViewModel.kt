package org.fmm.communitymgmt.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _userSession: UserSession
): ViewModel() {
    private val _userInfo = _userSession.userInfo!!
    val userInfo: UserInfoModel get() = _userInfo
}