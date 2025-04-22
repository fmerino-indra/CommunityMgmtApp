package org.fmm.communitymgmt.ui.security.google

interface AuthCallback {
    fun onSuccess(idToken: String)
    fun onError(error: String)
    fun onCanceled()
}