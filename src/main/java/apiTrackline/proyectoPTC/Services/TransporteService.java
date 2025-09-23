package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.ServicioTransporteEntity;
import apiTrackline.proyectoPTC.Entities.TransporteEntity;
import apiTrackline.proyectoPTC.Exceptions.ServicioTransporteExceptions.ExceptionServicioTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteRelacionado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransportistaNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOTransporte;
import apiTrackline.proyectoPTC.Repositories.ServicioTransporteRepository;
import apiTrackline.proyectoPTC.Repositories.TransporteRepository;
import apiTrackline.proyectoPTC.Repositories.TransportistaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import apiTrackline.proyectoPTC.Entities.TransportistaEntity;


@Slf4j
@Service
public class TransporteService {

    @Autowired
    private TransporteRepository transporteRepo;

    @Autowired
    private TransportistaRepository transportistaRepo;

    @Autowired
    private ServicioTransporteRepository servicioTransporteRepo;

    public Page<DTOTransporte> obtenerTransportes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransporteEntity> pageEntity = transporteRepo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    public DTOTransporte convertirADTO(TransporteEntity entity) {
        DTOTransporte dto = new DTOTransporte();
        dto.setIdTransporte(entity.getIdTransporte());

        if (entity.getTransportista() != null) {
            TransportistaEntity t = entity.getTransportista();
            dto.setIdTransportista(t.getIdTransportista());
            dto.setNombreTransportista(t.getNombre());
            dto.setApellidoTransportista(t.getApellido());
            dto.setTelefonoTransportista(t.getTelefono());
            dto.setCorreoTransportista(t.getCorreo());
            dto.setNitTransportista(t.getNit());
            dto.setIdUsuarioTransportista(t.getUsuarioT().getIdUsuario());
            dto.setNombreUsuario(t.getUsuarioT().getUsuario());
            dto.setContrasenia(t.getUsuarioT().getContrasenia());
        }

        if (entity.getServicioTransporte() != null) {
            ServicioTransporteEntity s = entity.getServicioTransporte();
            dto.setIdServicioTransporte(s.getIdServicioTransporte());
            dto.setPlacaServicio(s.getPlaca());
            dto.setTarjetaCirculacionServicio(s.getTarjetaCirculacion());
            dto.setCapacidadServicio(s.getCapacidad());
        }

        return dto;
    }

    public DTOTransporte agregarTransporte(DTOTransporte dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un transporte sin datos");
        }

        TransportistaEntity transportista = transportistaRepo.findById(dto.getIdTransportista())
                .orElseThrow(() -> new ExceptionTransportistaNoEncontrado("Transportista no encontrado con id " + dto.getIdTransportista()));

        ServicioTransporteEntity servicio = servicioTransporteRepo.findById(dto.getIdServicioTransporte())
                .orElseThrow(() -> new ExceptionServicioTransporteNoEncontrado("Servicio de transporte no encontrado con id " + dto.getIdServicioTransporte()));

        try {
            TransporteEntity entity = new TransporteEntity();
            entity.setTransportista(transportista);
            entity.setServicioTransporte(servicio);

            TransporteEntity transporteCreado = transporteRepo.save(entity);
            return convertirADTO(transporteCreado);
        } catch (Exception e) {
            log.error("Error al registrar el transporte: " + e.getMessage());
            throw new ExceptionTransporteNoRegistrado("Error interno: transporte no registrado, intente de nuevo");
        }
    }

    public DTOTransporte actualizarTransporte(Long id, DTOTransporte dto) {
        TransporteEntity transporte = transporteRepo.findById(id)
                .orElseThrow(() -> new ExceptionTransporteNoEncontrado("Transporte no encontrado con id " + id));

        if (dto.getIdTransportista() != null) {
            TransportistaEntity transportista = transportistaRepo.findById(dto.getIdTransportista())
                    .orElseThrow(() -> new ExceptionTransportistaNoEncontrado("Transportista no encontrado con id " + dto.getIdTransportista()));
            transporte.setTransportista(transportista);
        }

        if (dto.getIdServicioTransporte() != null) {
            ServicioTransporteEntity servicio = servicioTransporteRepo.findById(dto.getIdServicioTransporte())
                    .orElseThrow(() -> new ExceptionServicioTransporteNoEncontrado("Servicio de transporte no encontrado con id " + dto.getIdServicioTransporte()));
            transporte.setServicioTransporte(servicio);
        }

        return convertirADTO(transporteRepo.save(transporte));
    }

    public DTOTransporte patchTransporte(Long id, DTOTransporte dto) {
        TransporteEntity transporte = transporteRepo.findById(id)
                .orElseThrow(() -> new ExceptionTransporteNoEncontrado("Transporte no encontrado con id " + id));

        if (dto.getIdTransportista() != null) {
            TransportistaEntity transportista = transportistaRepo.findById(dto.getIdTransportista())
                    .orElseThrow(() -> new ExceptionTransportistaNoEncontrado("Transportista no encontrado con id " + dto.getIdTransportista()));
            transporte.setTransportista(transportista);
        }

        if (dto.getIdServicioTransporte() != null) {
            ServicioTransporteEntity servicio = servicioTransporteRepo.findById(dto.getIdServicioTransporte())
                    .orElseThrow(() -> new ExceptionServicioTransporteNoEncontrado("Servicio de transporte no encontrado con id " + dto.getIdServicioTransporte()));
            transporte.setServicioTransporte(servicio);
        }

        return convertirADTO(transporteRepo.save(transporte));
    }

    public String eliminarTransporte(Long id) {
        TransporteEntity transporte = transporteRepo.findById(id)
                .orElseThrow(() -> new ExceptionTransporteNoEncontrado("Transporte no encontrado con id " + id));
        try {
            transporteRepo.delete(transporte);
            return "Transporte eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionTransporteRelacionado("No se pudo eliminar el transporte porque tiene registros relacionados");
        }
    }

    public DTOTransporte buscarTransportePorId(Long id) {
        TransporteEntity entity = transporteRepo.findById(id)
                .orElseThrow(() -> new ExceptionTransporteNoEncontrado("No se encontró el transporte con ID: " + id));
        return convertirADTO(entity);
    }
}