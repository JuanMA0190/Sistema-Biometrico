package com.new19.ServerBiometric.repositorio;

import com.new19.ServerBiometric.entidades.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AsistenciaRepositorio extends JpaRepository<Asistencia, Long> {
    @Query("SELECT a FROM Asistencia a WHERE a.usuario.nLegajo = :nLegajo ORDER BY a.fecha DESC, a.horario DESC LIMIT 1")
    Optional<Asistencia> findUltimaAsistencia(@Param("nLegajo") Long nLegajo);
}
