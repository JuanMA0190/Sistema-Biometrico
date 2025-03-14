package com.example.applicationbiometric

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verificarCompatibilidadBiometrica()
    }

    private fun verificarCompatibilidadBiometrica() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Si el dispositivo admite biometría, mostrar el prompt de autenticación
                mostrarPromptBiometrico()
            }
            else -> {
                // Si no se puede autenticar, mostrar mensaje y cerrar la app
                Toast.makeText(this, "No se puede usar huella digital en este dispositivo", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun mostrarPromptBiometrico() {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    irAPantallaDeUsuario()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verificación biométrica")
            .setSubtitle("Coloca tu dedo en el sensor")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun irAPantallaDeUsuario() {
        runOnUiThread {
            val intent = Intent(this@MainActivity, UsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}