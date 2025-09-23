package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.*;
import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionEstadoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionOrdenServicioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.PermisosExceptions.ExceptionPermisoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeRelacionada;
import apiTrackline.proyectoPTC.Models.DTO.DTOClientes;
import apiTrackline.proyectoPTC.Models.DTO.DTOPermisos;
import apiTrackline.proyectoPTC.Models.DTO.DTOViaje;
import apiTrackline.proyectoPTC.Repositories.EstadosRepository;
import apiTrackline.proyectoPTC.Repositories.ViajeRepository;
import apiTrackline.proyectoPTC.Repositories.OrdenServicioRepository;
import apiTrackline.proyectoPTC.Repositories.TransporteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ViajeService {

    @Autowired
    private ViajeRepository repo;

    @Autowired
    private OrdenServicioRepository ordenRepo;

    @Autowired
    private TransporteRepository transporteRepo;

    @Autowired
    private EstadosRepository estadosRepository;

    // Obtener todos los viajes
    public List<DTOViaje> getAll() {
        List<ViajeEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //  GET paginado
    public Page<DTOViaje> obtenerViajes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ViajeEntity> viajes = repo.findAll(pageable);
        return viajes.map(this::convertirADTO);
    }

    // Convertir a DTO
    private DTOViaje convertirADTO(ViajeEntity entity) {
        DTOViaje dto = new DTOViaje();
        dto.setIdViaje(entity.getIdViaje());

        // --- CAMPOS PROPIOS DE VIAJE ---
        dto.setHoraEstimadaLlegada(entity.getHoraEstimadaLlegada());
        dto.setHoraLLegada(entity.getHoraLLegada());
        dto.setHoraSalida(entity.getHoraSalida());
        dto.setLugarPartida(entity.getLugarPartida());
        dto.setCoordenadaPartida(entity.getCoordenadaPartida());
        dto.setLugarLLegada(entity.getLugarLLegada());
        dto.setCoordenadaLlegada(entity.getCoordenadaLlegada());
        dto.setProgreso(entity.getProgreso());
        dto.setProgresoTrans(entity.getProgresoTrans());

        // --- ORDEN SERVICIO ---
        OrdenServicioEntity orden = entity.getOrdenServicio();
        if (orden != null) {
            dto.setIdOrdenServicio(orden.getIdOrdenServicio());
            dto.setClienteNIT(orden.getClienteNIT());

            if (orden.getCliente() != null) {
                dto.setNombreCliente(orden.getCliente().getNombre());
                dto.setApellidoCliente(orden.getCliente().getApellido());
                dto.setTelefonoCliente(orden.getCliente().getTelefono());
                dto.setCorreoCliente(orden.getCliente().getCorreo());
                dto.setCodEmpresaCliente(orden.getCliente().getCodEmpresa());

                if (orden.getCliente().getUsuario() != null) {
                    dto.setIdUsuarioCliente(orden.getCliente().getUsuario().getIdUsuario());
                    dto.setNombreUsuarioCliente(orden.getCliente().getUsuario().getUsuario());
                }
            }
        }

        // --- TRANSPORTE DEL VIAJE ---
        TransporteEntity transporte = entity.getTransporte();
        if (transporte != null) {
            dto.setIdTransporteViaje(transporte.getIdTransporte());

            if (transporte.getTransportista() != null) {
                var t = transporte.getTransportista();
                dto.setIdTransportistaViaje(t.getIdTransportista());
                dto.setNombreTransportistaViaje(t.getNombre());
                dto.setApellidoTransportistaViaje(t.getApellido());
                dto.setTelefonoTransportistaViaje(t.getTelefono());
                dto.setCorreoTransportistaViaje(t.getCorreo());
                dto.setNitTransportistaViaje(t.getNit());

                if (t.getUsuarioT() != null) {
                    dto.setIdUsuarioTransportistaViaje(t.getUsuarioT().getIdUsuario());
                    dto.setNombreUsuarioTransportistaViaje(t.getUsuarioT().getUsuario());
                }
            }

            if (transporte.getServicioTransporte() != null) {
                var st = transporte.getServicioTransporte();
                dto.setIdServicioTransporteViaje(st.getIdServicioTransporte());
                dto.setPlacaServicioViaje(st.getPlaca());
                dto.setTarjetaCirculacionServicioViaje(st.getTarjetaCirculacion());
                dto.setCapacidadServicioViaje(st.getCapacidad());
            }
        }
        //--- ESTADO ---
        if (entity.getIdEstado() != null) {
            EstadosEntity estado = entity.getIdEstado();
            dto.setIdEstado(estado.getIdEstado());

            // si EstadosEntity tiene relación con Selectivo
            if (estado.getSelectivo() != null) {
                dto.setIdSelectivo(estado.getSelectivo().getIdSelectivo());
                dto.setColorSelectivo(estado.getSelectivo().getColorSelectivo());
            }

            // ahora los demás campos que tienes en EstadosEntity
            dto.setDocumentos(estado.getDocumentos());
            dto.setClasificacion(estado.getClasificacion());
            dto.setDigitacion(estado.getDigitacion());
            dto.setRegistro(estado.getRegistro());
            dto.setPago(estado.getPago());
            dto.setLevantePago(estado.getLevantePago());
            dto.setEquipoTransporte(estado.getEquipoTransporte());
            dto.setCarga(estado.getCarga());
            dto.setEnCamino(estado.getEnCamino());
            dto.setEntregada(estado.getEntregada());
            dto.setFacturacion(estado.getFacturacion());
        }



        return dto;
    }


    public DTOViaje create(DTOViaje dto, Long idOrdenServicio, Long idTransporte, Long idEstado) {
        try {
            OrdenServicioEntity orden = ordenRepo.findById(idOrdenServicio)
                    .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                            "No se encontró orden de servicio con id: " + idOrdenServicio));

            TransporteEntity transporte = transporteRepo.findById(idTransporte)
                    .orElseThrow(() -> new ExceptionTransporteNoEncontrado(
                            "No se encontró transporte con id: " + idTransporte));

            EstadosEntity estados = estadosRepository.findById(idEstado)
                    .orElseThrow(() -> new ExceptionEstadoNoEncontrado(
                            "No se encontró estado con id: " + idEstado));

            ViajeEntity viaje = new ViajeEntity();
            viaje.setOrdenServicio(orden);
            viaje.setTransporte(transporte);
            viaje.setIdEstado(estados);

            // --- CAMPOS PROPIOS DE VIAJE ---
            viaje.setHoraEstimadaLlegada(dto.getHoraEstimadaLlegada());
            viaje.setHoraLLegada(dto.getHoraLLegada());
            viaje.setHoraSalida(dto.getHoraSalida());
            viaje.setLugarPartida(dto.getLugarPartida());
            viaje.setCoordenadaPartida(dto.getCoordenadaPartida());
            viaje.setLugarLLegada(dto.getLugarLLegada());
            viaje.setCoordenadaLlegada(dto.getCoordenadaLlegada());
            viaje.setProgreso(dto.getProgreso());
            viaje.setProgresoTrans(dto.getProgresoTrans());

            ViajeEntity guardado = repo.save(viaje);
            return convertirADTO(guardado);

        } catch (Exception e) {
            log.error("Error al crear viaje", e);
            throw new ExceptionViajeNoRegistrado("Error inesperado al registrar viaje");
        }
    }


    // Eliminar viaje
    public String delete(Long id) {
        ViajeEntity viaje = repo.findById(id)
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("No se encontró viaje con id: " + id));
        try {
            repo.delete(viaje);
            return "Viaje eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionViajeRelacionada("No se pudo eliminar el viaje porque tiene registros relacionados");
        }
    }

    // Actualización parcial (PATCH)
    public DTOViaje patch(Long id, DTOViaje dto) {
        ViajeEntity viaje = repo.findById(id)
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("No se encontró viaje con id: " + id));

        if (dto.getHoraEstimadaLlegada() != null) viaje.setHoraEstimadaLlegada(dto.getHoraEstimadaLlegada());
        if (dto.getHoraLLegada() != null) viaje.setHoraLLegada(dto.getHoraLLegada());
        if (dto.getHoraSalida() != null) viaje.setHoraSalida(dto.getHoraSalida());
        if (dto.getLugarPartida() != null) viaje.setLugarPartida(dto.getLugarPartida());
        if (dto.getCoordenadaPartida() != null) viaje.setCoordenadaPartida(dto.getCoordenadaPartida());
        if (dto.getLugarLLegada() != null) viaje.setLugarLLegada(dto.getLugarLLegada());
        if (dto.getCoordenadaLlegada() != null) viaje.setCoordenadaLlegada(dto.getCoordenadaLlegada());
        if (dto.getProgreso() != null) viaje.setProgreso(dto.getProgreso());
        if (dto.getProgresoTrans() != null) viaje.setProgresoTrans(dto.getProgresoTrans());

        return convertirADTO(repo.save(viaje));
    }


    // Actualización total (PUT)
    // PUT (actualización total)
    public DTOViaje putUpdate(Long id, DTOViaje dto, Long idOrdenServicio, Long idTransporte, Long idEstado) {
        ViajeEntity viaje = repo.findById(id)
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("No se encontró viaje con id: " + id));

        OrdenServicioEntity orden = ordenRepo.findById(idOrdenServicio)
                .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado("No se encontró orden de servicio con id: " + idOrdenServicio));

        TransporteEntity transporte = transporteRepo.findById(idTransporte)
                .orElseThrow(() -> new ExceptionTransporteNoEncontrado("No se encontró transporte con id: " + idTransporte));

        EstadosEntity estados = estadosRepository.findById(idEstado)
                .orElseThrow(() -> new ExceptionEstadoNoEncontrado("No se encontró estado con id: " + idEstado));

        viaje.setOrdenServicio(orden);
        viaje.setTransporte(transporte);
        viaje.setIdEstado(estados);

        // --- CAMPOS PROPIOS DE VIAJE ---
        viaje.setHoraEstimadaLlegada(dto.getHoraEstimadaLlegada());
        viaje.setHoraLLegada(dto.getHoraLLegada());
        viaje.setHoraSalida(dto.getHoraSalida());
        viaje.setLugarPartida(dto.getLugarPartida());
        viaje.setCoordenadaPartida(dto.getCoordenadaPartida());
        viaje.setLugarLLegada(dto.getLugarLLegada());
        viaje.setCoordenadaLlegada(dto.getCoordenadaLlegada());
        viaje.setProgreso(dto.getProgreso());
        viaje.setProgresoTrans(dto.getProgresoTrans());

        return convertirADTO(repo.save(viaje));
    }


    // Buscar por ID
    public DTOViaje buscarPorId(Long id) {
        ViajeEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("No se encontró viaje con id: " + id));
        return convertirADTO(entity);
    }
}
