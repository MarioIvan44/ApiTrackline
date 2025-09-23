package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.RolesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    Page<RolesEntity> findAll(Pageable pageable);
}
