package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.TransporteEntity;
import apiTrackline.proyectoPTC.Entities.TransportistaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransporteRepository extends JpaRepository<TransporteEntity, Long> {
    Page<TransporteEntity> findAll(Pageable pageable);

    @Query("SELECT t FROM TransporteEntity t WHERE t.transportista.correo = :correo")
    Optional<TransporteEntity> findByTransportistaCorreo(@Param("correo")String correo);
}
