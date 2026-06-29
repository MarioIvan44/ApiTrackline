package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Entities.ViajeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajeRepository extends JpaRepository<ViajeEntity, Long> {
    Page<ViajeEntity> findAll(Pageable pageable);

    @Query("""
    SELECT v FROM ViajeEntity v
    JOIN v.OrdenServicio o
    JOIN o.cliente c
    JOIN c.usuario u
    WHERE u.idUsuario = :idUsuario
""")
    Page<ViajeEntity> findByUsuarioId(@Param("idUsuario") Long idUsuario, Pageable pageable);

    @Query("""
    SELECT v FROM ViajeEntity v
    JOIN v.transporte t
    JOIN t.transportista tr
    JOIN tr.usuarioT u
    WHERE u.idUsuario = :idUsuario
""")
    Page<ViajeEntity> findByTransportistaUsuarioId(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
