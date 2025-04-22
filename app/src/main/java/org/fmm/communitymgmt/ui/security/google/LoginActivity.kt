package org.fmm.communitymgmt.ui.security.google

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var authManager: GoogleAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = GoogleAuthManager(this, null)

        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.btnLoginWithGoogle.setOnClickListener {
            startActivityForResult(
                authManager.getAuthIntent(),
                GoogleAuthManager.REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleAuthManager.REQUEST_CODE) {
            authManager.handleAuthResult(
                data,
                onSuccess = { idToken ->
                    Toast.makeText(this, "login OK\nToken: ${idToken}", Toast.LENGTH_LONG).show()
                    // Navegar a home
                },
                onError = {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    Log.e("LoginActivity", "Login error", it)
                }
            )
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authManager.dispose()
    }
}