package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Controllers.CargosController.Cargos;
import apiTrackline.proyectoPTC.Entities.CargosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargosRepository extends JpaRepository<CargosEntity, Long> {
    Page<CargosEntity> findAll(Pageable pageable);

    List<CargosEntity> findByOrdenServicioCargos_IdOrdenServicio(Long idOrdenServicio);
}
