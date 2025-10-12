package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.ClientesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientesRepository extends JpaRepository<ClientesEntity, String> {
    boolean existsByUsuario_IdUsuario(Long idUsuario);
    boolean existsByClienteNitAndUsuario_IdUsuario(String nitActual, Long idUsuario );
    List<ClientesEntity> findByNombreIgnoreCaseContaining(String nombre);
    Page<ClientesEntity> findAll(Pageable pageable);

    Optional<ClientesEntity> findByCorreo(String correo);
}
