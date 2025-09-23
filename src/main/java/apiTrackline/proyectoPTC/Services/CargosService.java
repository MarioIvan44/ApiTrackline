package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.CargosEntity;
import apiTrackline.proyectoPTC.Entities.OrdenServicioEntity;
import apiTrackline.proyectoPTC.Entities.TipoDatoContableEntity;
import apiTrackline.proyectoPTC.Exceptions.CargosExceptions.ExceptionCargoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.CargosExceptions.ExceptionCargoNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.CargosExceptions.ExceptionCargoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionOrdenServicioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TipoDatoContableExceptions.ExceptionTipoDatoContableNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOCargos;
import apiTrackline.proyectoPTC.Repositories.CargosRepository;
import apiTrackline.proyectoPTC.Repositories.OrdenServicioRepository;
import apiTrackline.proyectoPTC.Repositories.TipoDatoContableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class CargosService {

    @Autowired
    private CargosRepository repo;

    @Autowired
    private TipoDatoContableRepository tipoDatoRepo;

    @Autowired
    private OrdenServicioRepository ordenServicioRepo;

    // Obtener lista paginada
    public Page<DTOCargos> obtenerCargos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CargosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    // Buscar por ID
    public DTOCargos buscarCargoPorId(Long id) {
        CargosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionCargoNoEncontrado("Cargo no encontrado con id " + id));
        return convertirADTO(entity);
    }

    // Crear nuevo cargo
    public DTOCargos agregarCargo(DTOCargos dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos");
        }

        TipoDatoContableEntity tipo = tipoDatoRepo.findById(dto.getIdTipoDatoContable())
                .orElseThrow(() -> new ExceptionTipoDatoContableNoEncontrado(
                        "Tipo de dato contable no encontrado con id " + dto.getIdTipoDatoContable()));

        OrdenServicioEntity orden = null;
        if (dto.getIdOrdenServicio() != null) {
            orden = ordenServicioRepo.findById(dto.getIdOrdenServicio())
                    .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                            "Orden de servicio no encontrada con id " + dto.getIdOrdenServicio()));
        }

        try {
            CargosEntity entity = new CargosEntity();
            entity.setTipoDatoContable(tipo);
            entity.setOrdenServicioCargos(orden);
            entity.setMonto(dto.getMonto());
            entity.setCantidad(dto.getCantidad());

            CargosEntity creada = repo.save(entity);
            return convertirADTO(creada);
        } catch (Exception e) {
            log.error("Error al registrar el cargo: " + e.getMessage());
            throw new ExceptionCargoNoRegistrado("Error: Cargo no registrado");
        }
    }

    // 🔹 Actualizar (PUT)
    public DTOCargos actualizarCargo(Long id, DTOCargos dto) {
        CargosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionCargoNoEncontrado("Cargo no encontrado con id " + id));

        entity.setMonto(dto.getMonto());
        entity.setCantidad(dto.getCantidad());

        if (dto.getIdTipoDatoContable() != null) {
            TipoDatoContableEntity tipo = tipoDatoRepo.findById(dto.getIdTipoDatoContable())
                    .orElseThrow(() -> new ExceptionTipoDatoContableNoEncontrado(
                            "Tipo de dato contable no encontrado con id " + dto.getIdTipoDatoContable()));
            entity.setTipoDatoContable(tipo);
        }

        if (dto.getIdOrdenServicio() != null) {
            OrdenServicioEntity orden = ordenServicioRepo.findById(dto.getIdOrdenServicio())
                    .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                            "Orden de servicio no encontrada con id " + dto.getIdOrdenServicio()));
            entity.setOrdenServicioCargos(orden);
        }

        return convertirADTO(repo.save(entity));
    }

    // 🔹 Actualización parcial (PATCH)
    public DTOCargos patchCargo(Long id, DTOCargos dto) {
        CargosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionCargoNoEncontrado("Cargo no encontrado con id " + id));

        if (dto.getMonto() != null) entity.setMonto(dto.getMonto());
        if (dto.getCantidad() != null) entity.setCantidad(dto.getCantidad());

        if (dto.getIdTipoDatoContable() != null) {
            TipoDatoContableEntity tipo = tipoDatoRepo.findById(dto.getIdTipoDatoContable())
                    .orElseThrow(() -> new ExceptionTipoDatoContableNoEncontrado(
                            "Tipo de dato contable no encontrado con id " + dto.getIdTipoDatoContable()));
            entity.setTipoDatoContable(tipo);
        }

        if (dto.getIdOrdenServicio() != null) {
            OrdenServicioEntity orden = ordenServicioRepo.findById(dto.getIdOrdenServicio())
                    .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                            "Orden de servicio no encontrada con id " + dto.getIdOrdenServicio()));
            entity.setOrdenServicioCargos(orden);
        }

        return convertirADTO(repo.save(entity));
    }

    // 🔹 Eliminar
    public String eliminarCargo(Long id) {
        CargosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionCargoNoEncontrado("Cargo no encontrado con id " + id));

        try {
            repo.delete(entity);
            return "Cargo eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionCargoRelacionado(
                    "No se pudo eliminar el cargo porque tiene registros relacionados");
        }
    }

    // Conversión Entity → DTO
    private DTOCargos convertirADTO(CargosEntity entity) {
        DTOCargos dto = new DTOCargos();
        dto.setIdCargos(entity.getIdCargos());
        dto.setMonto(entity.getMonto());
        dto.setCantidad(entity.getCantidad());

        // TipoDatoContable
        if (entity.getTipoDatoContable() != null) {
            dto.setIdTipoDatoContable(entity.getTipoDatoContable().getIdTipoDatoContable());
            dto.setNombreTipoDatoContable(entity.getTipoDatoContable().getNombre());
        }

        // OrdenServicio
        if (entity.getOrdenServicioCargos() != null) {
            dto.setIdOrdenServicio(entity.getOrdenServicioCargos().getIdOrdenServicio());
            dto.setClienteNit(entity.getOrdenServicioCargos().getClienteNIT());
        }

        // Calcular total en memoria
        if (entity.getMonto() != null && entity.getCantidad() != null) {
            dto.setTotal(BigDecimal.valueOf(entity.getMonto()).multiply(BigDecimal.valueOf(entity.getCantidad())));
        } else {
            dto.setTotal(BigDecimal.ZERO);
        }

        return dto;
    }
}
