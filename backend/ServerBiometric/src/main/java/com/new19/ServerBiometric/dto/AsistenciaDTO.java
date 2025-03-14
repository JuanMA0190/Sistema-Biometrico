package com.new19.ServerBiometric.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AsistenciaDTO {
    @JsonProperty("nLegajo")
    private String nLegajo;

    @JsonProperty("nombreUsuario")
    private String nombreUsuario;

    @JsonProperty("contrasenia")
    private String contrasenia;

    public String getNLegajo() {
        return nLegajo;
    }

    public void setNLegajo(String nLegajo) {
        this.nLegajo = nLegajo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
