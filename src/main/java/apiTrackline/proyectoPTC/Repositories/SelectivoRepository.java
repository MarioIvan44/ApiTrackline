package apiTrackline.proyectoPTC.Repositories;


import apiTrackline.proyectoPTC.Entities.SelectivoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectivoRepository extends JpaRepository<SelectivoEntity, Long> {
    Page<SelectivoEntity> findAll(Pageable pageable);
}
