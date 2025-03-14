package com.new19.ServerBiometric.repositorio;

import com.new19.ServerBiometric.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
}
