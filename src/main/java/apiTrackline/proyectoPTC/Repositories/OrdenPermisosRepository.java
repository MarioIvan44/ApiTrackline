package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.OrdenPermisosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenPermisosRepository extends JpaRepository<OrdenPermisosEntity, Long> {
    Page<OrdenPermisosEntity> findAll(Pageable pageable);
}
