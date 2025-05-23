package org.fmm.communitymgmt.ui.common

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.fmm.communitymgmt.domainmodels.model.CommunityInfoModel
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    val userSession: UserSession): ViewModel() {

    fun initData() {
    }

    fun selectCommunity(it: CommunityInfoModel) {

    }

}