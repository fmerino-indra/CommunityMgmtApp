package org.fmm.communitymgmt.ui.comlist.detail

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship

data class EditPersonFormState (
    val name: String = "",
    val surname1: String = "",
    val surname2: String = "",
    val nickname: String = "",
    val phoneNumber: String = "",
    val emailAccount: String = "",
    val tinyPhoto: String = "",
    val mimeType: String = "",

    val nameError: String = "",
    val surname1Error: String = "",
    val surname2Error: String = "",
    val nicknameError: String = "",
    val phoneNumberError: String = "",
    val emailAccountError: String = "",
    val tinyPhotoError: String = "",
    val mimeTypeError: String = "",
    val isValid:Boolean = false)
