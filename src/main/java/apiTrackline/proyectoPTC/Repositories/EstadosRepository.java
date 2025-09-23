package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.EstadosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosRepository extends JpaRepository<EstadosEntity, Long>{
    Page<EstadosEntity> findAll(Pageable pageable);

}
