package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.PermisosEntity;
import apiTrackline.proyectoPTC.Entities.TipoFinanciamientosEntity;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoDuplicado;
import apiTrackline.proyectoPTC.Exceptions.TipoFinanciamientoExceptions.ExceptionTipoFinanciamientoDuplicado;
import apiTrackline.proyectoPTC.Exceptions.TipoFinanciamientoExceptions.ExceptionTipoFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TipoFinanciamientoExceptions.ExceptionTipoFinanciamientoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.TipoFinanciamientoExceptions.ExceptionTipoFinanciamientoRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoFinanciamientos;
import apiTrackline.proyectoPTC.Repositories.TipoFinanciamientosRepository;
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
public class TipoFinanciamientosService {

    @Autowired
    private TipoFinanciamientosRepository repo;

    // Obtener todos con paginación
    public Page<DTOTipoFinanciamientos> obtenerTiposFinanciamiento(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TipoFinanciamientosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirATipoFinanciamientoDTO);
    }

    // Convertir Entity → DTO
    public DTOTipoFinanciamientos convertirATipoFinanciamientoDTO(TipoFinanciamientosEntity entity) {
        DTOTipoFinanciamientos dto = new DTOTipoFinanciamientos();
        dto.setIdTipoFinanciamiento(entity.getIdTipoFinanciamiento());
        dto.setNombre(entity.getNombre());
        dto.setPrecioUni(entity.getPrecioUni());
        return dto;
    }

    // Convertir DTO → Entity (para registrar/actualizar)
    public TipoFinanciamientosEntity convertirADominio(DTOTipoFinanciamientos dto) {
        TipoFinanciamientosEntity entity = new TipoFinanciamientosEntity();
        entity.setIdTipoFinanciamiento(dto.getIdTipoFinanciamiento());
        entity.setNombre(dto.getNombre());
        entity.setPrecioUni(dto.getPrecioUni());
        return entity;
    }

    // Agregar nuevo
    public DTOTipoFinanciamientos agregarTipoFinanciamiento(DTOTipoFinanciamientos dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un registro vacío");
        }
        // Validar duplicados por nombre
        Optional<TipoFinanciamientosEntity> existente = repo.findByNombre(dto.getNombre());
        if (existente.isPresent()) {
            throw new ExceptionTipoFinanciamientoDuplicado("Ya existe un financiamiento con el nombre: " + dto.getNombre());
        }
        try {
            TipoFinanciamientosEntity entity = new TipoFinanciamientosEntity();
            entity.setNombre(dto.getNombre());
            entity.setPrecioUni(dto.getPrecioUni());

            TipoFinanciamientosEntity creado = repo.save(entity);
            return convertirATipoFinanciamientoDTO(creado);

        } catch (Exception e) {
            log.error("Error al registrar el Tipo de Financiamiento", e);
            throw new ExceptionTipoFinanciamientoNoRegistrado("Error: Tipo de financiamiento no registrado");
        }
    }

    //  Actualizar completo (PUT)
    public DTOTipoFinanciamientos actualizarTipoFinanciamiento(Long id, DTOTipoFinanciamientos dto) {
        TipoFinanciamientosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado("Tipo financiamiento no encontrado con id " + id));

        // Validar duplicado si cambia el nombre
        if (!entity.getNombre().equalsIgnoreCase(dto.getNombre()) &&
                repo.findByNombre(dto.getNombre()).isPresent()) {
            throw new ExceptionTipoFinanciamientoDuplicado("Ya existe un tipo financiamiento con el nombre: " + dto.getNombre());
        }
        entity.setNombre(dto.getNombre());
        entity.setPrecioUni(dto.getPrecioUni());
        return convertirATipoFinanciamientoDTO(repo.save(entity));
    }

    //  Actualizar parcial (PATCH)
    public DTOTipoFinanciamientos patchTipoFinanciamiento(Long id, DTOTipoFinanciamientos dto) {
        TipoFinanciamientosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado("Tipo financiamiento no encontrado con id " + id));

        if (dto.getNombre() != null) {
            if (dto.getNombre().isBlank()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            entity.setNombre(dto.getNombre());
        }

        if (dto.getPrecioUni() != null) {
            entity.setPrecioUni(dto.getPrecioUni());
        }

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            // Validar duplicado si cambia el nombre
            if (!entity.getNombre().equalsIgnoreCase(dto.getNombre()) &&
                    repo.findByNombre(dto.getNombre()).isPresent()) {
                throw new ExceptionTipoFinanciamientoDuplicado("Ya existe un tipo de financiamiento con el nombre: " + dto.getNombre());
            }
            entity.setNombre(dto.getNombre());
        }


        return convertirATipoFinanciamientoDTO(repo.save(entity));
    }

    // Eliminar
    public String eliminarTipoFinanciamiento(Long id) {
        TipoFinanciamientosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado("Tipo financiamiento no encontrado con id " + id));
        try {
            repo.delete(entity);
            return "Tipo de financiamiento eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionTipoFinanciamientoRelacionado("No se pudo eliminar porque está relacionado con otros registros");
        }
    }

    // Buscar por ID
    public DTOTipoFinanciamientos buscarPorId(Long id) {
        TipoFinanciamientosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado("No se encontró Tipo de financiamiento con ID: " + id));
        return convertirATipoFinanciamientoDTO(entity);
    }
}
