package com.example.applicationbiometric

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.applicationbiometric.model_dto.AsistenciaDTO
import com.example.applicationbiometric.model_dto.ServerResponse
import com.example.applicationbiometric.network.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody


class UsuarioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        val editNLegajo = findViewById<EditText>(R.id.etNlegajo)
        setupNumberValidation(editNLegajo)
        val editUsuario = findViewById<EditText>(R.id.etUsuario)
        val editPassword = findViewById<EditText>(R.id.etContrasenia)
        val btnEnviar = findViewById<Button>(R.id.btnConfirmar)

        btnEnviar.setOnClickListener {
            val nLegajo = editNLegajo.text.toString()
            val usuario = editUsuario.text.toString()
            val password = editPassword.text.toString()

            if (nLegajo.isNotEmpty() && usuario.isNotEmpty() && password.isNotEmpty()) {
                enviarDatosAlServidor(nLegajo, usuario, password)
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enviarDatosAlServidor(nLegajo: String, usuario: String, password: String) {
        val asistenciaDTO = AsistenciaDTO(nLegajo, usuario, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.registrarAsistencia(asistenciaDTO)

                withContext(Dispatchers.Main) {
                    Log.d("API_RESPONSE", "Código: ${response.code()}") // 🛠 LOG PARA DEPURAR

                    if (response.isSuccessful) {
                        val serverResponse = response.body()
                        val message = serverResponse?.mensaje ?: "Asistencia registrada exitosamente"
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                             finish()
                             overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                         }, 1000)
                    } else {
                        val errorMessage = parseErrorResponse(response.errorBody()) ?: "Error desconocido (${response.code()})"
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e("API_ERROR", "Código: ${response.code()}, Mensaje: $errorMessage") // 🛠 LOG PARA VER EL ERROR
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val errorMsg = "Error de conexión: ${e.message}"
                    Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_LONG).show()
                    Log.e("NETWORK_ERROR", errorMsg) // 🛠 LOG PARA ERRORES DE RED
                }
            }
        }
    }

    private fun setupNumberValidation(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    val input = it.toString()
                    // Verificar si contiene caracteres no numéricos
                    if (input.isNotEmpty() && !input.matches(Regex("^\\d+$"))) {
                        // Limpiar el campo y mostrar advertencia
                        editText.text.clear()
                        Toast.makeText(
                            this@UsuarioActivity,
                            "Solo se aceptan números en este campo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    // Función para parsear errores del servidor
    private fun parseErrorResponse(errorBody: ResponseBody?): String? {
        return try {
            val errorJson = errorBody?.string()
            val serverResponse = Gson().fromJson(errorJson, ServerResponse::class.java)
            serverResponse?.error ?: serverResponse?.mensaje
        } catch (e: Exception) {
            null
        }
    }
}