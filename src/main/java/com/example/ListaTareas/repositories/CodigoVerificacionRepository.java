package com.example.ListaTareas.repositories;
import com.example.ListaTareas.models.codigoVerificacion.CodigoVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CodigoVerificacionRepository extends JpaRepository<CodigoVerificacion, Long> {

    Optional<CodigoVerificacion> findByCodigo(String codigo);

    Boolean existsByCodigo(String codigo);

    List<CodigoVerificacion> findByActivoTrueAndExpiracionBefore(LocalDateTime localDateTime);
}
