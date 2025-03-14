package com.new19.ServerBiometric.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario extends Personal  {
    @Id
    private Long nLegajo;  // Identificador único (de Personal)

    private String nombreUsuario;
    private String contrasenia;

    // Getters y Setters
    public Long getNLegajo() {
        return nLegajo;
    }

    public void setNLegajo(Long nLegajo) {
        this.nLegajo = nLegajo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return contrasenia;
    }

    public void setPassword(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
