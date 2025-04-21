package org.fmm.communitymgmt.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ActivityInitBinding
import org.fmm.communitymgmt.ui.security.util.Auth0Manager
import org.fmm.communitymgmt.ui.security.util.EncryptedPrefsStorage
import org.fmm.communitymgmt.ui.security.util.SecureConfigManager
import javax.inject.Inject

class InitActivity: AppCompatActivity() {
    private lateinit var binding: ActivityInitBinding
    lateinit var auth0Manager: Auth0Manager
    lateinit var encryptedPrefsStorage: EncryptedPrefsStorage
    lateinit var secureConfigManager: SecureConfigManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariables()
        initUI()
    }

    private fun initVariables() {
        lifecycleScope.launch {
            encryptedPrefsStorage = EncryptedPrefsStorage(applicationContext)
            secureConfigManager = SecureConfigManager(applicationContext, encryptedPrefsStorage)
            secureConfigManager.ensureConfigImported()
            auth0Manager = Auth0Manager(applicationContext,encryptedPrefsStorage, secureConfigManager)
        }
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener {
            if (auth0Manager == null) {

            }
            auth0Manager.login(this, object: Callback<Credentials, AuthenticationException>{
                override fun onFailure(error: AuthenticationException) {
                    Toast.makeText(this@InitActivity, "Error login: ${error.message}", Toast
                        .LENGTH_SHORT).show()
                }

                override fun onSuccess(result: Credentials) {
                    Toast.makeText(this@InitActivity, "Login OK!: ${result.accessToken?.take(10)
                    }", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}