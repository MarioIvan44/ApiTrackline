package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.*;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaNoRegistrada;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionTipoFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOFinanciamiento;
import apiTrackline.proyectoPTC.Repositories.*;
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
public class FinanciamientoService {

    @Autowired
    private FinanciamientoRepository repo;

    @Autowired
    private TipoFinanciamientosRepository tipoRepo;

    @Autowired
    private OrdenServicioRepository ordenServicioRepo;

    //get sin paginación
    public List<DTOFinanciamiento> getSinPaginacion() {
        List<FinanciamientoEntity> financiamiento = repo.findAll();
        return empleados.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // LISTAR con paginación
    public Page<DTOFinanciamiento> obtenerFinanciamiento(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FinanciamientoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    // Convertir entidad a DTO
    private DTOFinanciamiento convertirADTO(FinanciamientoEntity entity) {
        DTOFinanciamiento dto = new DTOFinanciamiento();
        dto.setIdFinanciamiento(entity.getIdFinanciamiento());
        dto.setMonto(entity.getMonto());
        dto.setCantidad(entity.getCantidad());

        // Calcular total como virtual
        if (entity.getCantidad() != null && entity.getMonto() != null) {
            dto.setTotal(BigDecimal.valueOf(entity.getCantidad())
                    .multiply(BigDecimal.valueOf(entity.getMonto())));
        }

        // Relación con tipo de financiamiento
        if (entity.getTipoFinanciamiento() != null) {
            dto.setIdTipoFinanciamiento(entity.getTipoFinanciamiento().getIdTipoFinanciamiento());
            dto.setNombreTipoFinanciamiento(entity.getTipoFinanciamiento().getNombre());
        }

        // Relación con orden de servicio
        if (entity.getOrdenServicioFinanciamiento() != null) {
            dto.setIdOrdenServicio(entity.getOrdenServicioFinanciamiento().getIdOrdenServicio());
            dto.setClienteNIT(entity.getOrdenServicioFinanciamiento().getClienteNIT());
        }

        return dto;
    }

    // Agregar nuevo financiamiento
    public DTOFinanciamiento agregarFinanciamineto(DTOFinanciamiento json) {
        if (json == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos.");
        }

        // Buscar tipo de financiamiento
        TipoFinanciamientosEntity tipoFinanciamiento = tipoRepo.findById(json.getIdTipoFinanciamiento())
                .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado(
                        "Tipo financiamiento no encontrado con id " + json.getIdTipoFinanciamiento()));

        // Buscar orden de servicio
        OrdenServicioEntity ordenServicio = ordenServicioRepo.findById(json.getIdOrdenServicio())
                .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado(
                        "Orden servicio no encontrada con id " + json.getIdOrdenServicio()));

        try {
            FinanciamientoEntity entity = new FinanciamientoEntity();
            entity.setTipoFinanciamiento(tipoFinanciamiento);
            entity.setOrdenServicioFinanciamiento(ordenServicio);
            entity.setMonto(json.getMonto());
            entity.setCantidad(json.getCantidad());

            FinanciamientoEntity creado = repo.save(entity);
            return convertirADTO(creado);
        } catch (Exception e) {
            log.error("Error al registrar el financiamiento: {}", e.getMessage());
            throw new ExceptionAduanaNoRegistrada("Error: Financiamiento no registrado");
        }
    }

    // Actualizar completo
    public DTOFinanciamiento actualizarFinanciamiento(Long id, DTOFinanciamiento dto) {
        FinanciamientoEntity financiamiento = repo.findById(id)
                .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado("Financiamiento no encontrado con id " + id));

        financiamiento.setMonto(dto.getMonto());
        financiamiento.setCantidad(dto.getCantidad());

        if (dto.getIdTipoFinanciamiento() != null) {
            TipoFinanciamientosEntity tipoFinanciamiento = tipoRepo.findById(dto.getIdTipoFinanciamiento())
                    .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado(
                            "Tipo financiamiento no encontrado con id " + dto.getIdTipoFinanciamiento()));
            financiamiento.setTipoFinanciamiento(tipoFinanciamiento);
        }

        if (dto.getIdOrdenServicio() != null) {
            OrdenServicioEntity ordenServicio = ordenServicioRepo.findById(dto.getIdOrdenServicio())
                    .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado(
                            "Orden servicio no encontrada con id " + dto.getIdOrdenServicio()));
            financiamiento.setOrdenServicioFinanciamiento(ordenServicio);
        }

        return convertirADTO(repo.save(financiamiento));
    }

    // Patch (actualización parcial)
    public DTOFinanciamiento patchFinanciamiento(Long id, DTOFinanciamiento json) {
        FinanciamientoEntity financiamiento = repo.findById(id)
                .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado(
                        "Financiamiento no encontrado con id " + id));

        if (json.getMonto() != null) financiamiento.setMonto(json.getMonto());
        if (json.getCantidad() != null) financiamiento.setCantidad(json.getCantidad());

        if (json.getIdTipoFinanciamiento() != null) {
            TipoFinanciamientosEntity tipoFinanciamiento = tipoRepo.findById(json.getIdTipoFinanciamiento())
                    .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado(
                            "Tipo financiamiento no encontrado con id " + json.getIdTipoFinanciamiento()));
            financiamiento.setTipoFinanciamiento(tipoFinanciamiento);
        }

        if (json.getIdOrdenServicio() != null) {
            OrdenServicioEntity ordenServicio = ordenServicioRepo.findById(json.getIdOrdenServicio())
                    .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado(
                            "Orden servicio no encontrada con id " + json.getIdOrdenServicio()));
            financiamiento.setOrdenServicioFinanciamiento(ordenServicio);
        }

        return convertirADTO(repo.save(financiamiento));
    }

    // Eliminar
    public String eliminarFinanciamiento(Long id) {
        FinanciamientoEntity financiamiento = repo.findById(id)
                .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado(
                        "Financiamiento no encontrado con id " + id));
        try {
            repo.delete(financiamiento);
            return "Financiamiento eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionFinanciamientoRelacionado(
                    "No se pudo eliminar el financiamiento porque tiene registros relacionados");
        }
    }
}
