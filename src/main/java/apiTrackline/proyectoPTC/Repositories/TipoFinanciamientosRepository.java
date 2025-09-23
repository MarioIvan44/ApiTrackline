package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.PermisosEntity;
import apiTrackline.proyectoPTC.Entities.TipoFinanciamientosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository //Indica que hay in repositorio de métodos
public interface TipoFinanciamientosRepository extends JpaRepository<TipoFinanciamientosEntity, Long> {
    Page<TipoFinanciamientosEntity> findAll(Pageable pageable);
    Optional<TipoFinanciamientosEntity> findByNombre(String nombre);
}
