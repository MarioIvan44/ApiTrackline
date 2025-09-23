package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.RecoleccionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoleccionRepository extends JpaRepository<RecoleccionEntity, Long> {
    Page<RecoleccionEntity> findAll(Pageable pageable);
}
