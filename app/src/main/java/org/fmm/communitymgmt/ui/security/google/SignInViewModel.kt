package org.fmm.communitymgmt.ui.security.google

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
):ViewModel() {
    private val _signInState = MutableStateFlow<String?>(null)
    val signInSate: StateFlow<String?> = _signInState.asStateFlow()

    fun onGoogleCredentialReceived(idToken: String) {
        _signInState.value = "Token recibido: $idToken"
    }

    fun onSignInError(error:String) {
        _signInState.value = "Error: $error"
    }

}