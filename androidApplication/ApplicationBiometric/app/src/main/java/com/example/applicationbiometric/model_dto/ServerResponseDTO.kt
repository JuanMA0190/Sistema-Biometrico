package com.example.applicationbiometric.model_dto

import com.google.gson.annotations.SerializedName

data class ServerResponse(
    @SerializedName("mensaje") val mensaje: String?,  // Puede ser nulo si hay error
    @SerializedName("error") val error: String?      // Puede ser nulo si hay mensaje
)