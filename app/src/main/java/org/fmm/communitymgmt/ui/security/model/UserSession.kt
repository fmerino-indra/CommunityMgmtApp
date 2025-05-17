package org.fmm.communitymgmt.ui.security.model

import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject

class UserSession @Inject constructor () {
    var userInfo: UserInfoModel? = null
    fun isLoggedIn(): Boolean = userInfo != null
    fun isCommunitySelected(): Boolean = isLoggedIn() && userInfo?.community != null

    fun logout() {
        userInfo = null
        // @todo Limpiar tokens
    }
}