package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.TransportistaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportistaRepository extends JpaRepository<TransportistaEntity, Long> {
    boolean existsByUsuarioT_IdUsuario(Long idUsuario);
    boolean existsByUsuarioT_IdUsuarioAndIdTransportistaNot(Long idUsuario, Long idUsuarioActual);
    Page<TransportistaEntity> findAll(Pageable pageable);
}
