package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.*;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaNoEncontrada;
import apiTrackline.proyectoPTC.Exceptions.ClientesExceptions.ExceptionClienteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionOrdenServicioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.InfoEmbarqueExceptions.ExceptionInfoEmbarqueNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ObservacionesExceptions.ExceptionObservacionNoEncontrada;
import apiTrackline.proyectoPTC.Exceptions.OrdenEncabezadoExceptions.ExceptionOrdenEncabezadoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenServicioExceptions.ExceptionOrdenServicioNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenServicioExceptions.ExceptionOrdenServicioRelacionada;
import apiTrackline.proyectoPTC.Exceptions.RecoleccionExceptions.ExceptionRecoleccionNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOOrdenServicio;
import apiTrackline.proyectoPTC.Repositories.*;
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
public class OrdenServicioService {

    @Autowired
    private OrdenServicioRepository repo;
    @Autowired
    private ClientesRepository clientesRepository;
    @Autowired
    private OrdenEncabezadoRepository ordenEncabezadoRepo;
    @Autowired
    private InfoEmbarqueRepository infoEmbarqueRepo;
    @Autowired
    private AduanaRepository aduanaRepo;
    @Autowired
    private TransporteRepository transporteRepo;
    @Autowired
    private RecoleccionRepository recoleccionRepo;
    @Autowired
    private ObservacionesRepository observacionesRepo;

    // Obtener todos los registros y convertirlos a DTO
    public Page<DTOOrdenServicio> obtenerOrdenServicios(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdenServicioEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
        //TODO LO QUE SALE DE LA BASE SALE COMO ENTIDAD
        //TODO LO QUE ENTRA A LA BASE DEBE ENTRAR COMO ENTIDAD
    }

    private DTOOrdenServicio convertirADTO(OrdenServicioEntity entity) {
        DTOOrdenServicio dto = new DTOOrdenServicio();

        // OrdenServicio
        dto.setIdOrdenServicio(entity.getIdOrdenServicio());
        dto.setClienteNIT(entity.getClienteNIT());

        // Cliente y Usuario del Cliente
        if (entity.getCliente() != null) {
            dto.setNombreCliente(entity.getCliente().getNombre());
            dto.setApellidoCliente(entity.getCliente().getApellido());
            dto.setTelefonoCliente(entity.getCliente().getTelefono());
            dto.setCorreoCliente(entity.getCliente().getCorreo());
            dto.setCodEmpresaCliente(entity.getCliente().getCodEmpresa());

            if (entity.getCliente().getUsuario() != null) {
                dto.setIdUsuarioCliente(entity.getCliente().getUsuario().getIdUsuario());
                dto.setNombreUsuarioCliente(entity.getCliente().getUsuario().getUsuario());
            }
        }

        // Orden Encabezado
        if (entity.getIdOrdenEncabezado() != null) {
            dto.setIdOrdenEncabezado(entity.getIdOrdenEncabezado().getIdOrdenEncabezado());
            dto.setFechaOrden(entity.getIdOrdenEncabezado().getFecha());
            dto.setEncargadoUno(entity.getIdOrdenEncabezado().getEncargado());
            dto.setReferencia(entity.getIdOrdenEncabezado().getReferencia());
            dto.setImportador(entity.getIdOrdenEncabezado().getImportador());
            dto.setNitUno(entity.getIdOrdenEncabezado().getNit());
            dto.setRegistroIvaUno(entity.getIdOrdenEncabezado().getRegistroIva());
            dto.setFacturaA(entity.getIdOrdenEncabezado().getFacturaA());
            dto.setEncargadoDos(entity.getIdOrdenEncabezado().getEncargado2());
            dto.setNitDos(entity.getIdOrdenEncabezado().getNit2());
            dto.setRegistroIvaDos(entity.getIdOrdenEncabezado().getRegistroIva2());
        }

        // Info Embarque
        if (entity.getIdInfoEmbarque() != null) {
            dto.setIdInfoEmbarque(entity.getIdInfoEmbarque().getIdInfoEmbarque());
            dto.setFacturasEmbarque(entity.getIdInfoEmbarque().getFacturas());
            dto.setProveedorRefEmbarque(entity.getIdInfoEmbarque().getProveedorRef());
            dto.setBultosEmbarque(entity.getIdInfoEmbarque().getBultos());
            dto.setTipoEmbarque(entity.getIdInfoEmbarque().getTipo());
            dto.setKilosEmbarque(entity.getIdInfoEmbarque().getKilos());
            dto.setVolumenEmbarque(entity.getIdInfoEmbarque().getVolumen());
        }

        // Aduana
        if (entity.getAduana() != null) {
            dto.setIdAduana(entity.getAduana().getIdAduana());
            dto.setDm(entity.getAduana().getDM());
            dto.setPrimeraModalidad(entity.getAduana().getPrimeraModalidad());
            dto.setSegundaModalidad(entity.getAduana().getSegundaModalidad());
            dto.setDigitador(entity.getAduana().getDigitador());
            dto.setTramitador(entity.getAduana().getTramitador());

            if (entity.getAduana().getTipoServicio() != null) {
                dto.setIdTipoServicio(entity.getAduana().getTipoServicio().getIdTipoServicio());
                dto.setNombreTipoServicio(entity.getAduana().getTipoServicio().getTipoServicio());
            }
        }

        // Transporte
        if (entity.getIdTransporte() != null) {
            dto.setIdTransporte(entity.getIdTransporte().getIdTransporte());

            // Transportista
            if (entity.getIdTransporte().getTransportista() != null) {
                dto.setIdTransportista(entity.getIdTransporte().getTransportista().getIdTransportista());
                dto.setNombreTransportista(entity.getIdTransporte().getTransportista().getNombre());
                dto.setApellidoTransportista(entity.getIdTransporte().getTransportista().getApellido());
                dto.setTelefonoTransportista(entity.getIdTransporte().getTransportista().getTelefono());
                dto.setCorreoTransportista(entity.getIdTransporte().getTransportista().getCorreo());
                dto.setNitTransportista(entity.getIdTransporte().getTransportista().getNit());

                // Usuario del transportista
                if (entity.getIdTransporte().getTransportista().getUsuarioT() != null) {
                    dto.setIdUsuarioTransportista(entity.getIdTransporte().getTransportista().getUsuarioT().getIdUsuario());
                    dto.setNombreUsuarioTransportista(entity.getIdTransporte().getTransportista().getUsuarioT().getUsuario());
                }
            }

            // Servicio Transporte
            if (entity.getIdTransporte().getServicioTransporte() != null) {
                dto.setIdServicioTransporte(entity.getIdTransporte().getServicioTransporte().getIdServicioTransporte());
                dto.setPlacaServicio(entity.getIdTransporte().getServicioTransporte().getPlaca());
                dto.setTarjetaCirculacionServicio(entity.getIdTransporte().getServicioTransporte().getTarjetaCirculacion());
                dto.setCapacidadServicio(entity.getIdTransporte().getServicioTransporte().getCapacidad());
            }
        }

        // Recolección
        if (entity.getIdRecoleccion() != null) {
            dto.setIdRecoleccion(entity.getIdRecoleccion().getIdRecoleccion());
            dto.setTransporteRecoleccion(entity.getIdRecoleccion().getTransporte());
            dto.setRecoleccionEntregaRecoleccion(entity.getIdRecoleccion().getRecoleccionEntrega());
            dto.setNumeroDoc(entity.getIdRecoleccion().getNumeroDoc());
            dto.setLugarOrigen(entity.getIdRecoleccion().getLugarOrigen());
            dto.setPaisOrigen(entity.getIdRecoleccion().getPaisOrigen());
            dto.setLugarDestino(entity.getIdRecoleccion().getLugarDestino());
            dto.setPaisDestino(entity.getIdRecoleccion().getPaisDestino());
        }

        // Observaciones
        if (entity.getIdObservaciones() != null) {
            dto.setIdObservaciones(entity.getIdObservaciones().getIdObservaciones());
            dto.setTextoObservacion(entity.getIdObservaciones().getObservaciones());

            if (entity.getIdObservaciones().getIdSelectivo() != null) {
                dto.setIdSelectivo(entity.getIdObservaciones().getIdSelectivo().getIdSelectivo());
                dto.setColorSelectivo(entity.getIdObservaciones().getIdSelectivo().getColorSelectivo());
            }
        }

        return dto;
    }
    public DTOOrdenServicio compararIdOrdenYClienteNIT(Long idOrdenServicio, String clienteNIT) {
        if (idOrdenServicio == null) {
            throw new IllegalArgumentException("Debes ingresar el id de orden de servicio (no nulo)");
        }
        if (clienteNIT == null || clienteNIT.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes ingresar tu cliente NIT (no vacío)");
        }

        // Buscar orden de servicio
        OrdenServicioEntity ordenServicio = repo.findById(idOrdenServicio)
                .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                        "Orden servicio no encontrada con id " + idOrdenServicio));

        // Comparar ClienteNIT
        if (!clienteNIT.equals(ordenServicio.getClienteNIT())) {
            throw new IllegalArgumentException("El NIT proporcionado no coincide con el NIT de la orden de servicio");
        }

        // Si todo está bien, devolver el DTO
        return convertirADTO(ordenServicio);
    }
    public DTOOrdenServicio post(DTOOrdenServicio dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes crear una orden vacía");
        }

        try {
            OrdenServicioEntity entity = new OrdenServicioEntity();

            // Cliente (OBLIGATORIO)
            if (dto.getClienteNIT() != null && !dto.getClienteNIT().isBlank()) {
                ClientesEntity cliente = clientesRepository.findById(dto.getClienteNIT())
                        .orElseThrow(() -> new ExceptionClienteNoEncontrado(
                                "No se encontró cliente con NIT: " + dto.getClienteNIT()));
                entity.setCliente(cliente);
                entity.setClienteNIT(dto.getClienteNIT());
            } else {
                throw new ExceptionClienteNoEncontrado("El NIT del cliente es obligatorio");
            }

            // Lo demás es OPCIONAL → si no está, queda null
            if (dto.getIdOrdenEncabezado() != null) {
                entity.setIdOrdenEncabezado(
                        ordenEncabezadoRepo.findById(dto.getIdOrdenEncabezado()).orElse(null));
            }

            if (dto.getIdInfoEmbarque() != null) {
                entity.setIdInfoEmbarque(
                        infoEmbarqueRepo.findById(dto.getIdInfoEmbarque()).orElse(null));
            }

            if (dto.getIdAduana() != null) {
                entity.setAduana(
                        aduanaRepo.findById(dto.getIdAduana()).orElse(null));
            }

            if (dto.getIdTransporte() != null) {
                entity.setIdTransporte(
                        transporteRepo.findById(dto.getIdTransporte()).orElse(null));
            }

            if (dto.getIdRecoleccion() != null) {
                entity.setIdRecoleccion(
                        recoleccionRepo.findById(dto.getIdRecoleccion()).orElse(null));
            }

            if (dto.getIdObservaciones() != null) {
                entity.setIdObservaciones(
                        observacionesRepo.findById(dto.getIdObservaciones()).orElse(null));
            }

            // Guardar entidad
            OrdenServicioEntity guardado = repo.save(entity);

            // Devuelves DTO para consistencia con el resto del sistema
            return convertirADTO(guardado);

        } catch (ExceptionClienteNoEncontrado e) {
            throw e; // se propaga tal cual
        } catch (Exception e) {
            log.error("Error al crear orden de servicio", e);
            throw new ExceptionOrdenServicioNoRegistrado("Error inesperado al registrar orden");
        }
    }

    public DTOOrdenServicio update(Long id, DTOOrdenServicio dto) {
        OrdenServicioEntity entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de servicio no encontrada"));

        // Orden Encabezado
        if (dto.getIdOrdenEncabezado() != null) {
            OrdenEncabezadoEntity ordenEncabezado = ordenEncabezadoRepo.findById(dto.getIdOrdenEncabezado())
                    .orElseThrow(() -> new ExceptionOrdenEncabezadoNoEncontrado("Orden encabezado no encontrado con id " + dto.getIdTipoServicio()));
            entity.setIdOrdenEncabezado(ordenEncabezado);
        }

        // Info Embarque
        if (dto.getIdInfoEmbarque() != null) {
            InfoEmbarqueEntity infoEmbarque = infoEmbarqueRepo.findById(dto.getIdInfoEmbarque())
                    .orElseThrow(() -> new ExceptionInfoEmbarqueNoEncontrado("Info embarque no encontrado con id " + dto.getIdTipoServicio()));
            entity.setIdInfoEmbarque(infoEmbarque);
        }

        // Aduana
        if (dto.getIdAduana() != null) {
            AduanaEntity aduana = aduanaRepo.findById(dto.getIdAduana())
                    .orElseThrow(() -> new ExceptionAduanaNoEncontrada("Aduana no encontrada con id: " + dto.getIdAduana()));
            entity.setAduana(aduana);
        }

        // Transporte
        if (dto.getIdTransporte() != null) {
            TransporteEntity transporte = transporteRepo.findById(dto.getIdTransporte())
                    .orElseThrow(() -> new ExceptionTransporteNoEncontrado("Transporte no encontrado con id: " + dto.getIdTransporte()));
            entity.setIdTransporte(transporte);
        }

        // Recolección
        if (dto.getIdRecoleccion() != null) {
            RecoleccionEntity recoleccion = recoleccionRepo.findById(dto.getIdRecoleccion())
                        .orElseThrow(() -> new ExceptionRecoleccionNoEncontrado("Recolección no encontrado con id: " + dto.getIdRecoleccion()));
            entity.setIdRecoleccion(recoleccion);
        }

        // Observaciones
        if (dto.getIdObservaciones() != null) {
            ObservacionesEntity observaciones = observacionesRepo.findById(dto.getIdObservaciones())
                    .orElseThrow(() -> new ExceptionObservacionNoEncontrada("Observaciones no encontrado con id: " + dto.getIdObservaciones()));
            entity.setIdObservaciones(observaciones);
        }

        return convertirADTO(repo.save(entity));
    }

    public String patch(Long id, DTOOrdenServicio dto) {
        try {
            Optional<OrdenServicioEntity> optional = repo.findById(id);
            if (optional.isEmpty()) return "Error: Orden de servicio no encontrada";

            OrdenServicioEntity entity = optional.get();

            // Cliente
            if (dto.getClienteNIT() != null) {
                Optional<ClientesEntity> clientes = clientesRepository.findById(dto.getClienteNIT());
                if (clientes.isPresent()) {
                    entity.setCliente(clientes.get());
                    entity.setClienteNIT(dto.getClienteNIT());
                } else {
                    return "Error: Cliente no encontrado";
                }
            }

            // Orden Encabezado
            if (dto.getIdOrdenEncabezado() != null) {
                Optional<OrdenEncabezadoEntity> ordenEncabezado = ordenEncabezadoRepo.findById(dto.getIdOrdenEncabezado());
                if (ordenEncabezado.isPresent()) {
                    entity.setIdOrdenEncabezado(ordenEncabezado.get());
                } else {
                    return "Error: Orden encabezado no encontrado";
                }
            }

            // Info Embarque
            if (dto.getIdInfoEmbarque() != null) {
                Optional<InfoEmbarqueEntity> infoEmbarque = infoEmbarqueRepo.findById(dto.getIdInfoEmbarque());
                if (infoEmbarque.isPresent()) {
                    entity.setIdInfoEmbarque(infoEmbarque.get());
                } else {
                    return "Error: Info embarque no encontrada";
                }
            }

            // Aduana
            if (dto.getIdAduana() != null) {
                Optional<AduanaEntity> aduana = aduanaRepo.findById(dto.getIdAduana());
                if (aduana.isPresent()) {
                    entity.setAduana(aduana.get());
                } else {
                    return "Error: Aduana no encontrada";
                }
            }

            // Transporte
            if (dto.getIdTransporte() != null) {
                Optional<TransporteEntity> transporte = transporteRepo.findById(dto.getIdTransporte());
                if (transporte.isPresent()) {
                    entity.setIdTransporte(transporte.get());
                } else {
                    return "Error: Transporte no encontrado";
                }
            }

            // Recolección
            if (dto.getIdRecoleccion() != null) {
                Optional<RecoleccionEntity> recoleccion = recoleccionRepo.findById(dto.getIdRecoleccion());
                if (recoleccion.isPresent()) {
                    entity.setIdRecoleccion(recoleccion.get());
                } else {
                    return "Error: Recolección no encontrada";
                }
            }

            // Observaciones
            if (dto.getIdObservaciones() != null) {
                Optional<ObservacionesEntity> observaciones = observacionesRepo.findById(dto.getIdObservaciones());
                if (observaciones.isPresent()) {
                    entity.setIdObservaciones(observaciones.get());
                } else {
                    return "Error: Observaciones no encontradas";
                }
            }

            repo.save(entity);
            return "Orden de servicio actualizada parcialmente.";

        } catch (Exception e) {
            return "Error al hacer patch en orden de servicio: " + e.getMessage();
        }
    }


    public String eliminarOrdenServicio(Long id) {
        OrdenServicioEntity ordenServicioEntity = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado("Orden servicio no encontrado con id " + id));
        try {
            repo.delete(ordenServicioEntity);
            return "Orden servicio eliminada correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionOrdenServicioRelacionada("No se pudo eliminar la orden de servicio porque tiene registros relacionados");
        }
    }

    public DTOOrdenServicio buscarOrdenServicioPorId(Long id) {
        OrdenServicioEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado("No se encontró el orden servicio con ID: " + id));
        return convertirADTO(entity);
    }

    public Long obtenerUltimoIdOrdenServicio() {
        return repo.findTopByOrderByIdOrdenServicioDesc()
                .map(OrdenServicioEntity::getIdOrdenServicio)
                .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado("No existe ninguna orden de servicio registrada"));
    }

}