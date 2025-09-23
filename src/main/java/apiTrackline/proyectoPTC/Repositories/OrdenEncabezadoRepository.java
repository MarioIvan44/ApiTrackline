package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.OrdenEncabezadoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Indica que hay in repositorio de métodos
//Heredamos todo lo de JPARepository
public interface OrdenEncabezadoRepository extends JpaRepository<OrdenEncabezadoEntity, Long> {
    Page<OrdenEncabezadoEntity> findAll(Pageable pageable);
}