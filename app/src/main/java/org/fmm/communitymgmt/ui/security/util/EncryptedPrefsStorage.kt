package org.fmm.communitymgmt.ui.security.util

import android.content.Context
import com.auth0.android.authentication.storage.Storage
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.util.SecureStorage

class EncryptedPrefsStorage (context:Context): Storage {
    private var secureStorage: SecureStorage = SecureStorage(context, context.getString(R.string.secure_prefs_name))

    fun saveString(key: String, value:String) {
        secureStorage.saveEncryptedString(key, value)
    }

    fun getString(key:String, default:String?=null):String? {
        return secureStorage.getEncryptedString(key)?:default
    }

    fun saveBoolean(key:String, value:Boolean) {
        secureStorage.saveEncryptedBoolean(key, value)
    }

    fun getBoolean(key:String, default:Boolean?=false):Boolean {
        return secureStorage.getEncryptedBoolean(key)?:default!!
    }

    fun deleteValue(key:String) {
        secureStorage.deleteEncryptedValue(key)
    }

    fun clear() {
        secureStorage.clearAll()
    }

    override fun remove(name: String) {
        secureStorage.deleteEncryptedValue(name)
    }

    override fun retrieveBoolean(name: String): Boolean? {
        return secureStorage.getEncryptedBoolean(name)
    }

    override fun retrieveInteger(name: String): Int? {
        return secureStorage.getEncryptedInt(name)
    }

    override fun retrieveLong(name: String): Long? {
        return secureStorage.getEncryptedLong(name)
    }

    override fun retrieveString(name: String): String? {
        return secureStorage.getEncryptedString(name)
    }

    override fun store(name: String, value: Boolean?) {
        secureStorage.saveEncryptedBoolean(name, value?:false)
    }

    override fun store(name: String, value: Int?) {
        secureStorage.saveEncryptedInt(name, value?:0)
    }

    override fun store(name: String, value: Long?) {
        secureStorage.saveEncryptedLong(name, value?:0)
    }

    override fun store(name: String, value: String?) {
        secureStorage.saveEncryptedString(name, value?:"")
    }

}