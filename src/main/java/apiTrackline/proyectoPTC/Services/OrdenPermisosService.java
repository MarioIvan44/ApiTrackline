package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.OrdenPermisosEntity;
import apiTrackline.proyectoPTC.Entities.OrdenServicioEntity;
import apiTrackline.proyectoPTC.Entities.PermisosEntity;
import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionOrdenServicioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenPermisoExceptions.ExceptionOrdenPermisoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenPermisoExceptions.ExceptionOrdenPermisoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenPermisoExceptions.ExceptionOrdenPermisoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOOrdenPermisos;
import apiTrackline.proyectoPTC.Repositories.OrdenPermisosRepository;
import apiTrackline.proyectoPTC.Repositories.OrdenServicioRepository;
import apiTrackline.proyectoPTC.Repositories.PermisosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrdenPermisosService {

    @Autowired
    private OrdenPermisosRepository repo;

    @Autowired
    private PermisosRepository permisosRepo;

    @Autowired
    private OrdenServicioRepository ordenServiciosRepo;

    // Obtener lista paginada
    public Page<DTOOrdenPermisos> obtenerOrdenesPermisos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdenPermisosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    // Buscar por ID
    public DTOOrdenPermisos buscarOrdenPermisoPorId(Long id) {
        OrdenPermisosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenPermisoNoEncontrado(
                        "OrdenPermiso no encontrada con id " + id));
        return convertirADTO(entity);
    }

    // Crear nueva ordenPermiso
    public DTOOrdenPermisos agregarOrdenPermiso(DTOOrdenPermisos dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos");
        }

        PermisosEntity permiso = permisosRepo.findById(dto.getIdPermiso())
                .orElseThrow(() -> new ExceptionPermisoNoEncontrado(
                        "Permiso no encontrado con id " + dto.getIdPermiso()));

        OrdenServicioEntity ordenServicio = ordenServiciosRepo.findById(dto.getIdOrdenServicio())
                .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                        "Orden de servicio no encontrada con id " + dto.getIdOrdenServicio()));

        try {
            OrdenPermisosEntity entity = new OrdenPermisosEntity();
            entity.setMarcado(dto.getMarcado());
            entity.setIdPermiso(permiso);
            entity.setOrdenServicioPermisos(ordenServicio);

            OrdenPermisosEntity creada = repo.save(entity);
            return convertirADTO(creada);
        } catch (Exception e) {
            log.error("Error al registrar la ordenPermiso: " + e.getMessage());
            throw new ExceptionOrdenPermisoNoRegistrado("Error: OrdenPermiso no registrada");
        }
    }

    // 🔹 Actualizar (PUT)
    public DTOOrdenPermisos actualizarOrdenPermiso(Long id, DTOOrdenPermisos dto) {
        OrdenPermisosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenPermisoNoEncontrado(
                        "OrdenPermiso no encontrada con id " + id));

        entity.setMarcado(dto.getMarcado());

        if (dto.getIdPermiso() != null) {
            PermisosEntity permiso = permisosRepo.findById(dto.getIdPermiso())
                    .orElseThrow(() -> new ExceptionPermisoNoEncontrado(
                            "Permiso no encontrado con id " + dto.getIdPermiso()));
            entity.setIdPermiso(permiso);
        }

        if (dto.getIdOrdenServicio() != null) {
            OrdenServicioEntity ordenServicio = ordenServiciosRepo.findById(dto.getIdOrdenServicio())
                    .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                            "Orden de servicio no encontrada con id " + dto.getIdOrdenServicio()));
            entity.setOrdenServicioPermisos(ordenServicio);
        }

        return convertirADTO(repo.save(entity));
    }

    // 🔹 Actualización parcial (PATCH)
    public DTOOrdenPermisos patchOrdenPermiso(Long id, DTOOrdenPermisos dto) {
        OrdenPermisosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenPermisoNoEncontrado(
                        "OrdenPermiso no encontrada con id " + id));

        if (dto.getMarcado() != null) entity.setMarcado(dto.getMarcado());

        if (dto.getIdPermiso() != null) {
            PermisosEntity permiso = permisosRepo.findById(dto.getIdPermiso())
                    .orElseThrow(() -> new ExceptionPermisoNoEncontrado(
                            "Permiso no encontrado con id " + dto.getIdPermiso()));
            entity.setIdPermiso(permiso);
        }

        if (dto.getIdOrdenServicio() != null) {
            OrdenServicioEntity ordenServicio = ordenServiciosRepo.findById(dto.getIdOrdenServicio())
                    .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                            "Orden de servicio no encontrada con id " + dto.getIdOrdenServicio()));
            entity.setOrdenServicioPermisos(ordenServicio);
        }

        return convertirADTO(repo.save(entity));
    }

    // 🔹 Eliminar
    public String eliminarOrdenPermiso(Long id) {
        OrdenPermisosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenPermisoNoEncontrado(
                        "OrdenPermiso no encontrada con id " + id));

        try {
            repo.delete(entity);
            return "OrdenPermiso eliminada correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionOrdenPermisoRelacionado(
                    "No se pudo eliminar la OrdenPermiso porque tiene registros relacionados");
        }
    }

    // Conversión Entity → DTO
    private DTOOrdenPermisos convertirADTO(OrdenPermisosEntity entity) {
        DTOOrdenPermisos dto = new DTOOrdenPermisos();
        dto.setIdOrdenPermisos(entity.getIdOrdenPermisos());
        dto.setMarcado(entity.getMarcado());

        if (entity.getIdPermiso() != null) {
            dto.setIdPermiso(entity.getIdPermiso().getIdPermiso());
            dto.setNombrePermiso(entity.getIdPermiso().getNombrePermiso());
        }

        if (entity.getOrdenServicioPermisos() != null) {
            dto.setIdOrdenServicio(entity.getOrdenServicioPermisos().getIdOrdenServicio());
            dto.setClienteNIT(entity.getOrdenServicioPermisos().getClienteNIT());
        }

        return dto;
    }
}
