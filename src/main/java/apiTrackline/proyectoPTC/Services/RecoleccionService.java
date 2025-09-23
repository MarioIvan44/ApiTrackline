package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.RecoleccionEntity;
import apiTrackline.proyectoPTC.Exceptions.RecoleccionExceptions.ExceptionRecoleccionNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.RecoleccionExceptions.ExceptionRecoleccionNoRegistrada;
import apiTrackline.proyectoPTC.Exceptions.RecoleccionExceptions.ExceptionRecoleccionRelacionada;
import apiTrackline.proyectoPTC.Models.DTO.DTORecoleccion;
import apiTrackline.proyectoPTC.Repositories.RecoleccionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RecoleccionService {

    @Autowired
    private RecoleccionRepository repo;

    // Obtener lista paginada de recolecciones
    public Page<DTORecoleccion> obtenerRecolecciones(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecoleccionEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirARecoleccionDTO);
    }

    // Convertir entidad a DTO
    private DTORecoleccion convertirARecoleccionDTO(RecoleccionEntity entity) {
        DTORecoleccion dto = new DTORecoleccion();
        dto.setIdRecoleccion(entity.getIdRecoleccion());
        dto.setTransporte(entity.getTransporte());
        dto.setRecoleccionEntrega(entity.getRecoleccionEntrega());
        dto.setNumeroDoc(entity.getNumeroDoc());
        dto.setLugarOrigen(entity.getLugarOrigen());
        dto.setPaisOrigen(entity.getPaisOrigen());
        dto.setLugarDestino(entity.getLugarDestino());
        dto.setPaisDestino(entity.getPaisDestino());
        return dto;
    }

    // Agregar nueva recolección
    // Agregar nueva recolección
    public DTORecoleccion agregarRecoleccion(DTORecoleccion dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos");
        }
        try {
            RecoleccionEntity entity = new RecoleccionEntity();
            entity.setTransporte(dto.getTransporte());
            entity.setRecoleccionEntrega(dto.getRecoleccionEntrega());
            entity.setNumeroDoc(dto.getNumeroDoc());
            entity.setLugarOrigen(dto.getLugarOrigen());
            entity.setPaisOrigen(dto.getPaisOrigen());
            entity.setLugarDestino(dto.getLugarDestino());
            entity.setPaisDestino(dto.getPaisDestino());

            RecoleccionEntity guardada = repo.save(entity);
            return convertirARecoleccionDTO(guardada);
        } catch (DataIntegrityViolationException e) { // ⚡ Nuevo manejo
            log.error("Violación de integridad al registrar la recolección: " + e.getMessage());
            throw new ExceptionRecoleccionNoRegistrada("No se pudo registrar la recolección porque hay datos duplicados");
        }
    }

    // Actualizar toda la recolección
    public DTORecoleccion actualizarRecoleccion(Long id, DTORecoleccion dto) {
        RecoleccionEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionRecoleccionNoEncontrado("Recolección no encontrada con id " + id));

        entity.setTransporte(dto.getTransporte());
        entity.setRecoleccionEntrega(dto.getRecoleccionEntrega());
        entity.setNumeroDoc(dto.getNumeroDoc());
        entity.setLugarOrigen(dto.getLugarOrigen());
        entity.setPaisOrigen(dto.getPaisOrigen());
        entity.setLugarDestino(dto.getLugarDestino());
        entity.setPaisDestino(dto.getPaisDestino());

        try {
            return convertirARecoleccionDTO(repo.save(entity));
        } catch (DataIntegrityViolationException e) { // ⚡ Nuevo manejo
            log.error("Violación de integridad al actualizar la recolección: " + e.getMessage());
            throw new ExceptionRecoleccionNoRegistrada("No se pudo actualizar la recolección porque hay datos relacionados o duplicados");
        }
    }

    // Actualización parcial (PATCH)
    public DTORecoleccion patchRecoleccion(Long id, DTORecoleccion dto) {
        RecoleccionEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionRecoleccionNoEncontrado("Recolección no encontrada con id " + id));

        if (dto.getTransporte() != null) entity.setTransporte(dto.getTransporte());
        if (dto.getRecoleccionEntrega() != null) entity.setRecoleccionEntrega(dto.getRecoleccionEntrega());
        if (dto.getNumeroDoc() != null) entity.setNumeroDoc(dto.getNumeroDoc());
        if (dto.getLugarOrigen() != null) entity.setLugarOrigen(dto.getLugarOrigen());
        if (dto.getPaisOrigen() != null) entity.setPaisOrigen(dto.getPaisOrigen());
        if (dto.getLugarDestino() != null) entity.setLugarDestino(dto.getLugarDestino());
        if (dto.getPaisDestino() != null) entity.setPaisDestino(dto.getPaisDestino());

        try {
            return convertirARecoleccionDTO(repo.save(entity));
        } catch (DataIntegrityViolationException e) { // ⚡ Nuevo manejo
            log.error("Violación de integridad al aplicar patch en la recolección: " + e.getMessage());
            throw new ExceptionRecoleccionNoRegistrada("No se pudo aplicar la actualización parcial porque hay datos relacionados o duplicados");
        }
    }


    // Eliminar recolección por id
    public String eliminarRecoleccion(Long id) {
        RecoleccionEntity entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró información de recolección con id: " + id));
        try {
            repo.delete(entity);
            return "La información de recolección ha sido eliminada correctamente";
        } catch (DataIntegrityViolationException e) {
            log.error("Error al eliminar la recolección: " + e.getMessage());
            throw new ExceptionRecoleccionRelacionada("No se pudo eliminar la recolección porque tiene registros relacionados");
        }
    }

    // Buscar recolección por ID
    public DTORecoleccion buscarRecoleccionPorId(Long id) {
        RecoleccionEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionRecoleccionNoEncontrado("No se encontró la recolección con ID: " + id));
        return convertirARecoleccionDTO(entity);
    }
}

