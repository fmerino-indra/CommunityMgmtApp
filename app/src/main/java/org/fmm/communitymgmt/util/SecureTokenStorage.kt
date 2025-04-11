package org.fmm.communitymgmt.util

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecureTokenStorage(private val context: Context){
    private val keyAlias = "secureTokenKey"
    private val keystore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val masterKey: MasterKey by lazy {
        var keyGenParameterSpecBuilder = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            keyGenParameterSpecBuilder.setIsStrongBoxBacked(true)
        }
        MasterKey.Builder(context)
            .setKeyGenParameterSpec(keyGenParameterSpecBuilder.build())
            .build()
    }

    // Encrypt the token
    fun encryptToken(token:String):String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        }

        val iv = cipher.iv
        val encryption = cipher.doFinal(token.toByteArray())

        // CombiNar IV + datos cifrados para el almacenamiento
        val ivAndData = iv + encryption
        return ivAndData.joinToString ( "," ) { it.toString() }
    }

    // Decrypt the token
    fun decryptToken (encryptedToken: String): String {
        val ivAndData = encryptedToken.split(",").map { it.toByte() }
        val iv = ivAndData.take(12).toByteArray()
        val encryptedData = ivAndData.drop(12).toByteArray()

        val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
            val spec = GCMParameterSpec(128, iv)
            init(Cipher.DECRYPT_MODE, getOrCreateKey(), spec)
        }
        return String(cipher.doFinal(encryptedData))
    }

    // Get or create the encryption key from Keystore
    private fun getOrCreateKey(): SecretKey {
        return (keystore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry)?.secretKey
            ?: run {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore")
                keyGenerator.init(
                    KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT or
                            KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(256)
                        .build()
                )
                keyGenerator.generateKey()
        }
    }

    // Save encrypted token to EncryptedSharedPreferences
    fun saveEncryptedToken(token: String) {
        val encryptedToken = encryptToken(token)

        val encryptedPrefs = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        encryptedPrefs.edit().putString("token", encryptedToken).apply()
    }

    // Retrieve encrypted token from EncryptedSharedPreferences
    fun getEncryptedToken(): String? {
        val encryptedPrefs = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val encryptedToken = encryptedPrefs.getString("token", null)
        // Si encryptedToken != null ejecuta decryptToken(it)
        return encryptedToken?.let { decryptToken(it) }
    }
}