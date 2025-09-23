package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.EmpleadosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadosRepository extends JpaRepository<EmpleadosEntity, Long> {
    boolean existsByUsuarioEmpleado_IdUsuarioAndIdEmpleadoNot(Long idUsuario, Long idActual);
    boolean existsByUsuarioEmpleado_IdUsuario(Long idUsuario);
    Page<EmpleadosEntity> findAll(Pageable pageable);

}
