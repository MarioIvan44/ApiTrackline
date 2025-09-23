package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.OrdenServicioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenServicioRepository extends JpaRepository<OrdenServicioEntity, Long> {
    Page<OrdenServicioEntity> findAll(Pageable pageable);
    Optional<OrdenServicioEntity> findTopByOrderByIdOrdenServicioDesc();
}

