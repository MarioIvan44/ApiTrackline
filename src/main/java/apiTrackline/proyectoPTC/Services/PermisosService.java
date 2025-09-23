package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.PermisosEntity;
import apiTrackline.proyectoPTC.Entities.ViajeEntity;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.TipoFinanciamientoExceptions.ExceptionTipoFinanciamientoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.TipoFinanciamientoExceptions.ExceptionTipoFinanciamientoRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOPermisos;
import apiTrackline.proyectoPTC.Models.DTO.DTOViaje;
import apiTrackline.proyectoPTC.Repositories.PermisosRepository;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoDuplicado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PermisosService {

    @Autowired
    private PermisosRepository repo;

    //  GET paginado
    public Page<DTOPermisos> obtenerPermisos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PermisosEntity> permisos = repo.findAll(pageable);
        return permisos.map(this::convertirADTO);
    }

    // GET por ID
    public DTOPermisos buscarPorId(Long id) {
        PermisosEntity permiso = repo.findById(id)
                .orElseThrow(() -> new ExceptionPermisoNoEncontrado("No se encontró el permiso con id: " + id));
        return convertirADTO(permiso);
    }

    // POST - Crear permiso
    public DTOPermisos agregarPermiso(DTOPermisos dto) {
        // Validar duplicados por nombre
        Optional<PermisosEntity> existente = repo.findByNombrePermiso(dto.getNombrePermiso());
        if (existente.isPresent()) {
            throw new ExceptionPermisoDuplicado("Ya existe un permiso con el nombre: " + dto.getNombrePermiso());
        }

        try {
            PermisosEntity nuevo = convertirAEntity(dto);
            PermisosEntity guardado = repo.save(nuevo);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar el permiso", e);
            throw new ExceptionPermisoNoRegistrado("Error: Permiso no registrado");
        }
    }

    // PUT - Actualizar permiso completo
    public DTOPermisos actualizarPermiso(Long id, DTOPermisos dto) {
        PermisosEntity permiso = repo.findById(id)
                .orElseThrow(() -> new ExceptionPermisoNoEncontrado("No se encontró el permiso con id: " + id));

        // Validar duplicado si cambia el nombre
        if (!permiso.getNombrePermiso().equalsIgnoreCase(dto.getNombrePermiso()) &&
                repo.findByNombrePermiso(dto.getNombrePermiso()).isPresent()) {
            throw new ExceptionPermisoDuplicado("Ya existe un permiso con el nombre: " + dto.getNombrePermiso());
        }

        permiso.setNombrePermiso(dto.getNombrePermiso());
        PermisosEntity actualizado = repo.save(permiso);
        //log.info("Permiso actualizado con id {}", actualizado.getIdPermiso());
        return convertirADTO(actualizado);
    }

    // PATCH - Actualizar parcial
    public DTOPermisos patchPermiso(Long id, DTOPermisos dto) {
        PermisosEntity permiso = repo.findById(id)
                .orElseThrow(() -> new ExceptionPermisoNoEncontrado("No se encontró el permiso con id: " + id));

        if (dto.getNombrePermiso() != null && !dto.getNombrePermiso().isBlank()) {
            // Validar duplicado si cambia el nombre
            if (!permiso.getNombrePermiso().equalsIgnoreCase(dto.getNombrePermiso()) &&
                    repo.findByNombrePermiso(dto.getNombrePermiso()).isPresent()) {
                throw new ExceptionPermisoDuplicado("Ya existe un permiso con el nombre: " + dto.getNombrePermiso());
            }
            permiso.setNombrePermiso(dto.getNombrePermiso());
        }

        PermisosEntity actualizado = repo.save(permiso);
        //log.info("Permiso actualizado parcialmente con id {}", actualizado.getIdPermiso());
        return convertirADTO(actualizado);
    }

    // DELETE - Eliminar permiso
    public String eliminarPermiso(Long id) {
        PermisosEntity permiso = repo.findById(id)
                .orElseThrow(() -> new ExceptionPermisoNoEncontrado("No se encontró el permiso con id: " + id));
        try {
            repo.delete(permiso);
            return "Permiso eliminado correctamente con id " + id;
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionPermisoRelacionado("No se pudo eliminar porque está relacionado con otros registros");
        }

    }

    // ==========================
    // MÉTODOS AUXILIARES
    // ==========================
    private DTOPermisos convertirADTO(PermisosEntity entity) {
        DTOPermisos dto = new DTOPermisos();
        dto.setIdPermiso(entity.getIdPermiso());
        dto.setNombrePermiso(entity.getNombrePermiso());
        return dto;
    }

    private PermisosEntity convertirAEntity(DTOPermisos dto) {
        PermisosEntity entity = new PermisosEntity();
        entity.setIdPermiso(dto.getIdPermiso()); // normalmente null en POST
        entity.setNombrePermiso(dto.getNombrePermiso());
        return entity;
    }
}
