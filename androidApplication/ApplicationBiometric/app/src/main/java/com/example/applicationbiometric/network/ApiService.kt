package com.example.applicationbiometric.network

import com.example.applicationbiometric.model_dto.AsistenciaDTO
import com.example.applicationbiometric.model_dto.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/asistencia/registrar")
    suspend fun registrarAsistencia(@Body asistenciaDTO: AsistenciaDTO): Response<ServerResponse>
}