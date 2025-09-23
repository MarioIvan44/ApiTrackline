package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.AduanaEntity;
import apiTrackline.proyectoPTC.Entities.FinanciamientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanciamientoRepository extends JpaRepository<FinanciamientoEntity, Long> {
    Page<FinanciamientoEntity> findAll(Pageable pageable);
}
