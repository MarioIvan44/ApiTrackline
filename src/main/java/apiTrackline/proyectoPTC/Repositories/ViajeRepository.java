package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.ViajeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajeRepository extends JpaRepository<ViajeEntity, Long> {
    Page<ViajeEntity> findAll(Pageable pageable);
}
