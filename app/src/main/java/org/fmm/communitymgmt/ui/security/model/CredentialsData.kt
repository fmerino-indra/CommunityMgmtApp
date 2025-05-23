package org.fmm.communitymgmt.ui.security.model

data class CredentialsData (
    val id: String,val idToken: String, val displayName:String?,
    val familyName: String?, val givenName: String?, val profilePictureUri: android.net.Uri?, val
    phoneNumber: String?, val providerName: String, val email: String){

}
