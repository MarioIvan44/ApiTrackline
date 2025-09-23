package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.PermisosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisosRepository extends JpaRepository <PermisosEntity, Long> {
    Page<PermisosEntity> findAll(Pageable pageable);
    // Para validar duplicados por nombre
    Optional<PermisosEntity> findByNombrePermiso(String nombrePermiso);
}
