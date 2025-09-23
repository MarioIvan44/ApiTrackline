package apiTrackline.proyectoPTC.Repositories;

import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Indica que hay in repositorio de métodos

//Heredamos todo lo de JPARepository
//Practicamente ya está hecho el CRUD hecho con esto
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>  {
    boolean existsByUsuario(String usuario); //Verifica que no haya un nombre de usuario igual en la base de datps
    boolean existsByUsuarioAndIdUsuarioNot(String usuario, Long idUsuario); //verificar si existe otro usuario con ese nombre, pero con un ID diferente.
    Optional<UsuarioEntity> findByUsuario(String usuario); // agregado

    Page<UsuarioEntity> findAll(Pageable pageable);

}
