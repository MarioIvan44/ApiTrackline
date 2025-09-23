package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.TransporteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransporteRepository extends JpaRepository<TransporteEntity, Long> {
    Page<TransporteEntity> findAll(Pageable pageable);
}
