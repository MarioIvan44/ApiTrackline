package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.TipoClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoClienteRepository extends JpaRepository<TipoClienteEntity, Long> {

}
