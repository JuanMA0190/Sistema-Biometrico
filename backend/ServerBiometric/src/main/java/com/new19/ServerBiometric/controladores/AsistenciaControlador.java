package com.new19.ServerBiometric.controladores;

import com.new19.ServerBiometric.dto.AsistenciaDTO;
import com.new19.ServerBiometric.entidades.Asistencia;
import com.new19.ServerBiometric.entidades.EstadoPersonal;
import com.new19.ServerBiometric.entidades.Usuario;
import com.new19.ServerBiometric.repositorio.AsistenciaRepositorio;
import com.new19.ServerBiometric.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaControlador {
    @Autowired
    private AsistenciaRepositorio asistenciaRepository;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarAsistencia(@RequestBody AsistenciaDTO dto) {
        System.out.println("DTO recibido: nLegajo=" + dto.getNLegajo() +
                ", nombreUsuario=" + dto.getNombreUsuario() +
                ", contrasenia=" + dto.getContrasenia());

        Map<String, String> response = new HashMap<>();

        if (dto.getNLegajo() == null || dto.getNombreUsuario() == null || dto.getContrasenia() == null) {
            response.put("error", "nLegajo, nombreUsuario y contrasenia no pueden ser nulos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            Long legajo = Long.parseLong(dto.getNLegajo());
            Optional<Usuario> usuarioOpt = usuarioRepository.findUsuarioConEstado(legajo);

            if (!usuarioOpt.isPresent()) {
                response.put("error", "Usuario no encontrado con nLegajo: " + dto.getNLegajo());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Usuario usuario = usuarioOpt.get();

            if (!usuario.getNombreUsuario().equalsIgnoreCase(dto.getNombreUsuario())) {
                response.put("error", "Nombre de usuario incorrecto");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!BCrypt.checkpw(dto.getContrasenia(), usuario.getPassword())) {
                response.put("error", "Contraseña incorrecta");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 🔴 VALIDACIÓN DEL ESTADO
            if (usuario.getEstado() == EstadoPersonal.INACTIVO) {
                response.put("error", "El usuario está INACTIVO y no puede registrar asistencia");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            Asistencia asistencia = new Asistencia();
            asistencia.setUsuario(usuario);
            asistencia.setFecha(LocalDate.now());
            asistencia.setHorario(LocalTime.now());

            asistenciaRepository.save(asistencia);

            response.put("mensaje", "✅ Asistencia registrada correctamente");
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            response.put("error", "El nLegajo debe ser un número válido: " + dto.getNLegajo());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
