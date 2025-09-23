package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.AduanaEntity;
import apiTrackline.proyectoPTC.Entities.ObservacionesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservacionesRepository extends JpaRepository<ObservacionesEntity, Long> {
    Page<ObservacionesEntity> findAll(Pageable pageable);
}

