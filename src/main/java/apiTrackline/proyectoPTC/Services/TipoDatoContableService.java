package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.TipoDatoContableEntity;
import apiTrackline.proyectoPTC.Exceptions.TipoDatoContableExceptions.ExceptionTipoDatoContableNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TipoDatoContableExceptions.ExceptionTipoDatoContableNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.TipoDatoContableExceptions.ExceptionTipoDatoContableRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoDatoContable;
import apiTrackline.proyectoPTC.Repositories.TipoDatoContableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TipoDatoContableService {

    @Autowired
    private TipoDatoContableRepository repo;

    // Obtener paginación
    public Page<DTOTipoDatoContable> obtenerTiposContables(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TipoDatoContableEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirAContableDTO);
    }

    // Conversión entidad → DTO
    public DTOTipoDatoContable convertirAContableDTO(TipoDatoContableEntity entity) {
        DTOTipoDatoContable dto = new DTOTipoDatoContable();
        dto.setIdTipoDatoContable(entity.getIdTipoDatoContable());
        dto.setNombre(entity.getNombre());
        dto.setPrecioUni(entity.getPrecioUni());
        return dto;
    }

    // Conversión DTO → entidad
    private TipoDatoContableEntity convertirADatoContableEntity(DTOTipoDatoContable dto) {
        TipoDatoContableEntity entity = new TipoDatoContableEntity();
        entity.setIdTipoDatoContable(dto.getIdTipoDatoContable());
        entity.setNombre(dto.getNombre());
        entity.setPrecioUni(dto.getPrecioUni());
        return entity;
    }

    //  Agregar registro
    public DTOTipoDatoContable agregarTipoContable(DTOTipoDatoContable dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes registrar un tipo de dato contable vacío");
        }
        try {
            TipoDatoContableEntity entity = convertirADatoContableEntity(dto);
            TipoDatoContableEntity creado = repo.save(entity);
            return convertirAContableDTO(creado);
        } catch (Exception e) {
            log.error("Error al registrar tipo de dato contable: " + e);
            throw new ExceptionTipoDatoContableNoRegistrado("Error: tipo de dato contable no registrado");
        }
    }

    // 🔹 Actualizar registro
    public DTOTipoDatoContable actualizarTipoContable(Long id, DTOTipoDatoContable dto) {
        TipoDatoContableEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoDatoContableNoEncontrado("No se encontró el tipo de dato contable con id " + id));

        entity.setNombre(dto.getNombre());
        entity.setPrecioUni(dto.getPrecioUni());

        return convertirAContableDTO(repo.save(entity));
    }

    // 🔹 Patch (solo actualiza lo enviado)
    public DTOTipoDatoContable patchTipoContable(Long id, DTOTipoDatoContable dto) {
        TipoDatoContableEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoDatoContableNoEncontrado("No se encontró el tipo de dato contable con id " + id));

        if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
        if (dto.getPrecioUni() != null) entity.setPrecioUni(dto.getPrecioUni());
        return convertirAContableDTO(repo.save(entity));
    }

    // 🔹 Eliminar
    public String eliminarTipoContable(Long id) {
        TipoDatoContableEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoDatoContableNoEncontrado("No se encontró el tipo de dato contable con id " + id));
        try {
            repo.delete(entity);
            return "Tipo de dato contable eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionTipoDatoContableRelacionado("No se puede eliminar: está relacionado con otros datos");
        }
    }

    // 🔹 Buscar por id
    public DTOTipoDatoContable buscarTipoContablePorId(Long id) {
        TipoDatoContableEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoDatoContableNoEncontrado("No se encontró el tipo de dato contable con id " + id));
        return convertirAContableDTO(entity);
    }
}
