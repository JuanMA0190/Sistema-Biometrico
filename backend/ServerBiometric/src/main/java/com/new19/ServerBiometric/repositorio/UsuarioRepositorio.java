package com.new19.ServerBiometric.repositorio;

import com.new19.ServerBiometric.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.nLegajo = :nLegajo")
    Optional<Usuario> findUsuarioConEstado(@Param("nLegajo") Long nLegajo);
}
