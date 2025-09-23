package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.CargosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargosRepository extends JpaRepository<CargosEntity, Long> {
    Page<CargosEntity> findAll(Pageable pageable);
}
