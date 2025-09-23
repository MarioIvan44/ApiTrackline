package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.ServicioTransporteEntity;
import apiTrackline.proyectoPTC.Exceptions.ServicioTransporteExceptions.ExceptionServicioTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ServicioTransporteExceptions.ExceptionServicioTransporteNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.ServicioTransporteExceptions.ExceptionServicioTransporteRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOServicioTransporte;
import apiTrackline.proyectoPTC.Repositories.ServicioTransporteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServicioTransporteService {

    @Autowired
    private ServicioTransporteRepository repo;

    public Page<DTOServicioTransporte> obtenerServiciosTransporte(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServicioTransporteEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    /**
     * Conversión de Entidad a DTO
     */
    private DTOServicioTransporte convertirADTO(ServicioTransporteEntity entity) {
        DTOServicioTransporte dto = new DTOServicioTransporte();
        dto.setIdServicioTransporte(entity.getIdServicioTransporte());
        dto.setPlaca(entity.getPlaca());
        dto.setTarjetaCirculacion(entity.getTarjetaCirculacion());
        dto.setCapacidad(entity.getCapacidad());
        return dto;
    }

    /**
     * Registrar servicio transporte (validando duplicados en placa y tarjeta)
     */
    public DTOServicioTransporte agregarServicioTransporte(DTOServicioTransporte json) {
        if (json == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos");
        }

        // Validar que no exista una Placa duplicada
        if (repo.existsByPlaca(json.getPlaca())) {
            throw new ExceptionServicioTransporteNoRegistrado("Ya existe un servicio con la placa: " + json.getPlaca());
        }

        // Validar que no exista una Tarjeta de Circulación duplicada
        if (repo.existsByTarjetaCirculacion(json.getTarjetaCirculacion())) {
            throw new ExceptionServicioTransporteNoRegistrado("Ya existe un servicio con la tarjeta de circulación: " + json.getTarjetaCirculacion());
        }

        try {
            ServicioTransporteEntity entity = new ServicioTransporteEntity();
            entity.setPlaca(json.getPlaca());
            entity.setTarjetaCirculacion(json.getTarjetaCirculacion());
            entity.setCapacidad(json.getCapacidad());

            ServicioTransporteEntity creado = repo.save(entity);
            return convertirADTO(creado);

        } catch (Exception e) {
            log.error("Error al registrar el servicio de transporte: ", e);
            throw new ExceptionServicioTransporteNoRegistrado("Error: servicio de transporte no registrado");
        }
    }

    /**
     * Actualizar (validando duplicados si cambian placa o tarjeta)
     */
    public DTOServicioTransporte actualizarServicioTransporte(Long id, DTOServicioTransporte json) {
        ServicioTransporteEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionServicioTransporteNoEncontrado("Servicio de transporte no encontrado con id " + id));

        // Validar duplicados SOLO si se cambia la placa
        if (!entity.getPlaca().equals(json.getPlaca()) && repo.existsByPlaca(json.getPlaca())) {
            throw new ExceptionServicioTransporteNoRegistrado("Ya existe un servicio con la placa: " + json.getPlaca());
        }

        // Validar duplicados SOLO si se cambia la tarjeta
        if (!entity.getTarjetaCirculacion().equals(json.getTarjetaCirculacion()) && repo.existsByTarjetaCirculacion(json.getTarjetaCirculacion())) {
            throw new ExceptionServicioTransporteNoRegistrado("Ya existe un servicio con la tarjeta de circulación: " + json.getTarjetaCirculacion());
        }

        entity.setPlaca(json.getPlaca());
        entity.setTarjetaCirculacion(json.getTarjetaCirculacion());
        entity.setCapacidad(json.getCapacidad());

        return convertirADTO(repo.save(entity));
    }

    /**
     * Actualización parcial (PATCH) validando duplicados
     */
    public DTOServicioTransporte patchServicioTransporte(Long id, DTOServicioTransporte json) {
        ServicioTransporteEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionServicioTransporteNoEncontrado("Servicio de transporte no encontrado con id " + id));

        if (json.getPlaca() != null && !entity.getPlaca().equals(json.getPlaca()) && repo.existsByPlaca(json.getPlaca())) {
            throw new ExceptionServicioTransporteNoRegistrado("Ya existe un servicio con la placa: " + json.getPlaca());
        }

        if (json.getTarjetaCirculacion() != null && !entity.getTarjetaCirculacion().equals(json.getTarjetaCirculacion())
                && repo.existsByTarjetaCirculacion(json.getTarjetaCirculacion())) {
            throw new ExceptionServicioTransporteNoRegistrado("Ya existe un servicio con la tarjeta de circulación: " + json.getTarjetaCirculacion());
        }

        if (json.getPlaca() != null) entity.setPlaca(json.getPlaca());
        if (json.getTarjetaCirculacion() != null) entity.setTarjetaCirculacion(json.getTarjetaCirculacion());
        if (json.getCapacidad() != null) entity.setCapacidad(json.getCapacidad());

        return convertirADTO(repo.save(entity));
    }

    /**
     * Eliminar registro por ID
     */
    public String eliminarServicioTransporte(Long id) {
        ServicioTransporteEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionServicioTransporteNoEncontrado("Servicio de transporte no encontrado con id " + id));
        try {
            repo.delete(entity);
            return "Servicio de transporte eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionServicioTransporteRelacionado("No se pudo eliminar el servicio de transporte porque tiene registros relacionados");
        }
    }

    public DTOServicioTransporte buscarServicioTransportePorId(Long id) {
        ServicioTransporteEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionServicioTransporteNoEncontrado("No se encontró el servicio de transporte con ID: " + id));
        return convertirADTO(entity);
    }
}
