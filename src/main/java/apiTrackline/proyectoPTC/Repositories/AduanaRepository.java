package apiTrackline.proyectoPTC.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import apiTrackline.proyectoPTC.Entities.AduanaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AduanaRepository extends JpaRepository<AduanaEntity, Long> {
    Page<AduanaEntity> findAll(Pageable pageable);
}

