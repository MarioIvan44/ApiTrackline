package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.ObservacionesEntity;
import apiTrackline.proyectoPTC.Entities.SelectivoEntity;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionObservacionNoEncontrada;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionObservacionNoRegistrada;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionObservacionRelacionada;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionSelectivoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOObservaciones;
import apiTrackline.proyectoPTC.Repositories.ObservacionesRepository;
import apiTrackline.proyectoPTC.Repositories.SelectivoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ObservacionesService {

    @Autowired
    private ObservacionesRepository repo;

    @Autowired
    private SelectivoRepository selectivoRepo;

    // Obtener lista paginada de observaciones
    public Page<DTOObservaciones> obtenerObservaciones(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ObservacionesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirAObservacionDTO);
    }

    // Convertir Entity a DTO
    public DTOObservaciones convertirAObservacionDTO(ObservacionesEntity entity) {
        DTOObservaciones dto = new DTOObservaciones();
        dto.setIdObservaciones(entity.getIdObservaciones());
        dto.setObservaciones(entity.getObservaciones());

        if (entity.getIdSelectivo() != null) {
            dto.setIdSelectivo(entity.getIdSelectivo().getIdSelectivo());
            dto.setColorSelectivo(entity.getIdSelectivo().getColorSelectivo());
        } else {
            dto.setIdSelectivo(null);
            dto.setColorSelectivo(null);
        }
        return dto;
    }

    // Crear nueva observación
    public DTOObservaciones agregarObservacion(DTOObservaciones json) {
        if (json == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos");
        }

        SelectivoEntity selectivo = selectivoRepo.findById(json.getIdSelectivo())
                .orElseThrow(() -> new ExceptionSelectivoNoEncontrado(
                        "Selectivo no encontrado con id " + json.getIdSelectivo()));

        try {
            ObservacionesEntity entity = new ObservacionesEntity();
            entity.setObservaciones(json.getObservaciones());
            entity.setIdSelectivo(selectivo);

            ObservacionesEntity creada = repo.save(entity);
            return convertirAObservacionDTO(creada);
        } catch (Exception e) {
            log.error("Error al registrar la observación: " + e.getMessage());
            throw new ExceptionObservacionNoRegistrada("Error: observación no registrada");
        }
    }

    // Actualizar observación (PUT)
    public DTOObservaciones actualizarObservacion(Long id, DTOObservaciones dto) {
        ObservacionesEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionObservacionNoEncontrada(
                        "Observación no encontrada con id " + id));

        entity.setObservaciones(dto.getObservaciones());

        if (dto.getIdSelectivo() != null) {
            SelectivoEntity selectivo = selectivoRepo.findById(dto.getIdSelectivo())
                    .orElseThrow(() -> new ExceptionSelectivoNoEncontrado(
                            "Selectivo no encontrado con id " + dto.getIdSelectivo()));
            entity.setIdSelectivo(selectivo);
        }

        return convertirAObservacionDTO(repo.save(entity));
    }

    // Actualización parcial (PATCH)
    public DTOObservaciones patchObservacion(Long id, DTOObservaciones dto) {
        ObservacionesEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionObservacionNoEncontrada(
                        "Observación no encontrada con id " + id));

        if (dto.getObservaciones() != null) entity.setObservaciones(dto.getObservaciones());

        if (dto.getIdSelectivo() != null) {
            SelectivoEntity selectivo = selectivoRepo.findById(dto.getIdSelectivo())
                    .orElseThrow(() -> new ExceptionSelectivoNoEncontrado(
                            "Selectivo no encontrado con id " + dto.getIdSelectivo()));
            entity.setIdSelectivo(selectivo);
        }

        return convertirAObservacionDTO(repo.save(entity));
    }

    // Eliminar observación
    public String eliminarObservacion(Long id) {
        ObservacionesEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionObservacionNoEncontrada(
                        "Observación no encontrada con id " + id));

        try {
            repo.delete(entity);
            return "Observación eliminada correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionObservacionRelacionada("No se pudo eliminar la observación porque tiene registros relacionados");
        }
    }

    // Buscar por ID
    public DTOObservaciones buscarObservacionPorId(Long id) {
        ObservacionesEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionObservacionNoEncontrada(
                        "No se encontró la observación con ID: " + id));
        return convertirAObservacionDTO(entity);
    }
}