package com.example.applicationbiometric.model_dto

import com.google.gson.annotations.SerializedName

data class AsistenciaDTO(
    @field:SerializedName("nLegajo") val nLegajo: String,
    @field:SerializedName("nombreUsuario") val nombreUsuario: String,
    @field:SerializedName("contrasenia") val contrasenia: String
)