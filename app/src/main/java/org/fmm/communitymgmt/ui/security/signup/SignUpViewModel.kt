package org.fmm.communitymgmt.ui.security.signup

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.domainlogic.usecase.GetUserInfo
import org.fmm.communitymgmt.ui.security.util.EncryptedPrefsStorage
import org.fmm.communitymgmt.util.StringResourcesProvider
import java.security.SecureRandom
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val stringResourcesProvider: StringResourcesProvider,
    private val getUserInfo: GetUserInfo,
    private val encryptedPrefsStorage: EncryptedPrefsStorage
):ViewModel() {
    private val _uiSignUpState = MutableStateFlow<SignUpUIState>(SignUpUIState.Loading)
    val signUpSate: StateFlow<SignUpUIState> = _uiSignUpState

    private val _formSignUpState = MutableStateFlow<SignUpFormState>(SignUpFormState())
    private val formSignUpState: StateFlow<SignUpFormState> = _formSignUpState

}