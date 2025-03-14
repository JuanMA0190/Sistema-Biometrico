package com.example.applicationbiometric

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        // Aquí decides cómo obtener el nLegajo; por ejemplo, para esta prueba, usamos el dni
        // Crea el objeto AsistenciaDTO
        val asistenciaDTO = AsistenciaDTO(nLegajo = nLegajo, nombreUsuario = usuario, contrasenia = password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.registrarAsistencia(asistenciaDTO)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val serverResponse = response.body()
                        val message = serverResponse?.mensaje
                            ?: serverResponse?.error
                            ?: "Asistencia registrada exitosamente"

                        Toast.makeText(
                            applicationContext,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()

                        // Cierra la actividad después de 1 segundo (para que se vea el Toast)
                        Handler(Looper.getMainLooper()).postDelayed({
                            finish()
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }, 1000)
                    } else {
                        // Manejar errores HTTP (4xx, 5xx)
                        val errorMessage = parseErrorResponse(response.errorBody())
                            ?: "Error desconocido (Código ${response.code()})"

                        Toast.makeText(
                            applicationContext,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Error de conexión: ${e.message ?: "Verifica tu conexión a Internet"}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
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