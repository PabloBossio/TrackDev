package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.RegistroTiempo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroTiempoRepository extends JpaRepository<RegistroTiempo, Long> {

    List<RegistroTiempo> findByTareaId(Long tareaId);

    List<RegistroTiempo> findByUsuarioId(Long usuarioId);
}