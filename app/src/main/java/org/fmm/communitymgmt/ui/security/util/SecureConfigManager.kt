package org.fmm.communitymgmt.ui.security.util

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.R
import org.json.JSONObject

class SecureConfigManager constructor(
    private val context: Context,
    private val encryptedPrefsStorage: EncryptedPrefsStorage
) {
    companion object {
        private const val KEY_IMPORTED = "config_imported"
        private const val KEY_CLIENT_ID = "client_id"
        private const val KEY_DOMAIN = "domain"
    }

//    private val ioScope = CoroutineScope((Dispatchers.IO + SupervisorJob()))

    suspend fun ensureConfigImported () {
/* ioScope.launch {
Lanza una nueva corrutina independiente en segundo plano.
No espera su resultado.
Si se lanza desde un init {} o cualquier parte no suspendida, no hay forma sencilla de saber cuándo ha terminado.
Ideal para tareas que pueden ir por su cuenta (ej: sincronización en segundo plano, logs, etc.).
 */

/* withContext(Dispatchers.IO) {
Puedes esperar de forma segura.
No bloqueas la UI.
La lógica es determinística: sabes cuándo ha terminado.
 */
        withContext(Dispatchers.IO) {
            if (!encryptedPrefsStorage.getBoolean(KEY_IMPORTED, false)) {
                importConfigFromAssets()
            }
        }
    }

    private suspend fun importConfigFromAssets() {
        try {
            val json = withContext(Dispatchers.IO) {
                context.assets.open(context.getString(R.string.config_json_name)).bufferedReader().use{
                    it.readText()
                }
            }
            val jsonObject = JSONObject(json)
            val clientId = jsonObject.getString(context.getString(R.string.config_json_client_id))
            val domain = jsonObject.getString(context.getString(R.string.config_json_backend_domain))

            encryptedPrefsStorage.saveString(KEY_CLIENT_ID, clientId)
            encryptedPrefsStorage.saveString(KEY_DOMAIN, domain)
        } catch (e: Exception) {
            Log.e("SecureConfigManager", "Error importing config file", e)
        }
    }

    fun getClientId():String? = encryptedPrefsStorage.getString(KEY_CLIENT_ID, null)

    fun getDomain():String? = encryptedPrefsStorage.getString(KEY_DOMAIN, null)
}