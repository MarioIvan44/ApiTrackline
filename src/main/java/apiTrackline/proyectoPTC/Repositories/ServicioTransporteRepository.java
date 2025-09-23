package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.ServicioTransporteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface    ServicioTransporteRepository extends JpaRepository<ServicioTransporteEntity, Long> {
    Page<ServicioTransporteEntity> findAll(Pageable pageable);
    boolean existsByPlaca(String placa);
    boolean existsByTarjetaCirculacion(String tarjetaCirculacion);
}
