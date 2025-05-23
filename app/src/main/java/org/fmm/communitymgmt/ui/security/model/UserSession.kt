package org.fmm.communitymgmt.ui.security.model

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor () {
    private var _sessionData: SessionData? = null
    private var _loggedData: LoggedData? = null

    /**
     * Aunque se declaran como nulables, al tener el operador elvis: ?:, actúa como no nulable
     */
    val sessionData get() = _sessionData ?: error("Active session, but no user info")
    val loggedData get() = _loggedData ?: error("No active session")

    val userInfo get() = sessionData.userInfo
    val credential get() = loggedData.credential
/*
    fun initialize(userInfo: UserInfoModel, credential: GoogleIdTokenCredential) {
        require(_sessionData == null) {"Session already initialized"}
        _sessionData = DataWrapper.SessionData(credential,userInfo)
    }

 */
    fun initialize(userInfo: UserInfoModel) {
        require(_sessionData == null) {"Session already initialized"}
        _sessionData = SessionData(userInfo)
    }

    fun initialize(credential: CredentialsData) {
        require(_loggedData == null) {"Session already initialized"}
        _loggedData = LoggedData(credential)
    }

    //fun get(): SessionData = _sessionData ?: error("No active session")

    /**
     * Funciones para comprobar el estado pero sin que lance excepción.
     */
    fun isFullLoggedIn(): Boolean = _sessionData !=null
    fun isLoggedIn(): Boolean = _loggedData !=null

    fun logout() {
        _sessionData = null
        _loggedData = null
    }
}

data class LoggedData(val credential: CredentialsData)
data class SessionData(val userInfo:UserInfoModel)
/*
sealed class DataWrapper(open val credential: GoogleIdTokenCredential) {
    data class LoggedData(override val credential: GoogleIdTokenCredential) : DataWrapper
        (credential)
    data class SessionData(override val credential: GoogleIdTokenCredential, val userInfo:
    UserInfoModel): DataWrapper(credential)
}

 */

//data class SessionData(val userInfo: UserInfoModel, val credential: GoogleIdTokenCredential)
