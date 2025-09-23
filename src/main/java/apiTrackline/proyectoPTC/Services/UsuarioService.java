package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Config.Argon2.Argon2Password;
import apiTrackline.proyectoPTC.Entities.RolesEntity;
import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Exceptions.UsuarioExceptions.ExceptionRolNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.UsuarioExceptions.ExceptionUsuarioDuplicado;
import apiTrackline.proyectoPTC.Exceptions.UsuarioExceptions.ExceptionUsuarioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.UsuarioExceptions.ExceptionUsuarioNoRegistrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOUsuario;
import apiTrackline.proyectoPTC.Repositories.RolesRepository;
import apiTrackline.proyectoPTC.Repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private RolesRepository rolesRepo;

    @Autowired
    private Argon2Password argon2Password;

    // Paginación y obtención de todos los usuarios
    public Page<DTOUsuario> obtenerUsuarios(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirAUsuarioDTO);
    }

    // Buscar usuario por ID
    public DTOUsuario buscarUsuarioPorId(Long id) {
        UsuarioEntity user = repo.findById(id)
                .orElseThrow(() -> new ExceptionUsuarioNoEncontrado("Usuario no encontrado con ID: " + id));
        return convertirAUsuarioDTO(user);
    }

    // Convertir entidad a DTO
    private DTOUsuario convertirAUsuarioDTO(UsuarioEntity user) {
        DTOUsuario dto = new DTOUsuario();
        dto.setIdUsuario(user.getIdUsuario());
        dto.setUsuario(user.getUsuario());
        dto.setContrasenia(user.getContrasenia()); // hash
        if (user.getRol() != null) {
            dto.setIdRol(user.getRol().getIdRol());
            dto.setRol(user.getRol().getRol());
        } else {
            dto.setIdRol(null);
            dto.setRol(null);
        }
        return dto;
    }

    // Crear usuario
    public DTOUsuario agregarUsuario(DTOUsuario dtoUser) {
        if (dtoUser == null) {
            throw new IllegalArgumentException("No puedes agregar un usuario sin datos");
        }

        if (repo.existsByUsuario(dtoUser.getUsuario())) {
            throw new ExceptionUsuarioDuplicado("El nombre de usuario ya existe");
        }

        RolesEntity rol = rolesRepo.findById(dtoUser.getIdRol())
                .orElseThrow(() -> new ExceptionRolNoEncontrado("Rol no encontrado con ID: " + dtoUser.getIdRol()));

        try {
            UsuarioEntity user = new UsuarioEntity();
            user.setUsuario(dtoUser.getUsuario());


            String hash = argon2Password.EncryptPassword(dtoUser.getContrasenia());
            user.setContrasenia(hash);

            user.setRol(rol);

            UsuarioEntity creado = repo.save(user);
            return convertirAUsuarioDTO(creado);

        } catch (Exception e) {
            log.error("Error al crear usuario: ", e);
            throw new ExceptionUsuarioNoRegistrado("Error: usuario no registrado");
        }
    }

    // Actualizar usuario (PUT)
    public DTOUsuario actualizarUsuario(Long id, DTOUsuario dtoUser) {
        UsuarioEntity user = repo.findById(id)
                .orElseThrow(() -> new ExceptionUsuarioNoEncontrado("Usuario no encontrado con id " + id));

        if (repo.existsByUsuarioAndIdUsuarioNot(dtoUser.getUsuario(), id)) {
            throw new ExceptionUsuarioDuplicado("El nombre de usuario ya está en uso");
        }

        user.setUsuario(dtoUser.getUsuario());

        if (dtoUser.getContrasenia() != null && !dtoUser.getContrasenia().isBlank()) {
            String hash = argon2Password.EncryptPassword(dtoUser.getContrasenia());
            user.setContrasenia(hash);
        }

        if (dtoUser.getIdRol() != null) {
            RolesEntity rol = rolesRepo.findById(dtoUser.getIdRol())
                    .orElseThrow(() -> new ExceptionRolNoEncontrado("Rol no encontrado con id " + dtoUser.getIdRol()));
            user.setRol(rol);
        }

        return convertirAUsuarioDTO(repo.save(user));
    }

    // Patch usuario (actualización parcial)
    public DTOUsuario patchUsuario(Long id, DTOUsuario dtoUser) {
        UsuarioEntity user = repo.findById(id)
                .orElseThrow(() -> new ExceptionUsuarioNoEncontrado("Usuario no encontrado con id " + id));

        if (dtoUser.getUsuario() != null) {
            if (repo.existsByUsuarioAndIdUsuarioNot(dtoUser.getUsuario(), id)) {
                throw new ExceptionUsuarioDuplicado("El nombre de usuario ya está en uso");
            }
            user.setUsuario(dtoUser.getUsuario());
        }

        if (dtoUser.getContrasenia() != null && !dtoUser.getContrasenia().isBlank()) {
            String hash = argon2Password.EncryptPassword(dtoUser.getContrasenia());
            user.setContrasenia(hash);
        }

        if (dtoUser.getIdRol() != null) {
            RolesEntity rol = rolesRepo.findById(dtoUser.getIdRol())
                    .orElseThrow(() -> new ExceptionRolNoEncontrado("Rol no encontrado con id " + dtoUser.getIdRol()));
            user.setRol(rol);
        }

        return convertirAUsuarioDTO(repo.save(user));
    }

    // Eliminar usuario
    public String eliminarUsuario(Long id) {
        UsuarioEntity user = repo.findById(id)
                .orElseThrow(() -> new ExceptionUsuarioNoEncontrado("Usuario no encontrado con id " + id));
        try {
            repo.delete(user);
            return "Usuario eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("No se pudo eliminar el usuario porque tiene registros relacionados");
        }
    }

    // Buscar usuario por nombre
    public DTOUsuario buscarUsuarioPorNombre(String nombreUsuario) {
        UsuarioEntity user = repo.findByUsuario(nombreUsuario)
                .orElseThrow(() -> new ExceptionUsuarioNoEncontrado("Usuario no encontrado con nombre: " + nombreUsuario));
        return convertirAUsuarioDTO(user);
    }

}
