package com.new19.ServerBiometric.entidades;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "personal")
public class Personal implements Serializable {

    @Id
    @Column(name = "n_legajo", unique = true, nullable = false)
    private Long nLegajo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(name = "n_telefono")
    private String nTelefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPersonal estado;

    // Getters y Setters
    public Long getNLegajo() {
        return nLegajo;
    }

    public void setNLegajo(Long nLegajo) {
        this.nLegajo = nLegajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNTelefono() {
        return nTelefono;
    }

    public void setNTelefono(String nTelefono) {
        this.nTelefono = nTelefono;
    }

    public EstadoPersonal getEstado() {
        return estado;
    }

    public void setEstado(EstadoPersonal estado) {
        this.estado = estado;
    }
}
