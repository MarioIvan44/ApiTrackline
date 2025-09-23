package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.ClientesEntity;
import apiTrackline.proyectoPTC.Entities.TipoClienteEntity;
import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Exceptions.ClientesExceptions.*;
import apiTrackline.proyectoPTC.Models.DTO.DTOClientes;
import apiTrackline.proyectoPTC.Repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientesService {

    @Autowired private ClientesRepository repo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private EmpleadosRepository empleadosRepo;
    @Autowired private TransportistaRepository transportistasRepo;
    @Autowired private TipoClienteRepository tipoClienteRepo;

    public List<DTOClientes> obtenerSinPaginacion(){
        List<ClientesEntity> clientes = repo.findAll();
        return clientes.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }
    public Page<DTOClientes> obtenerClientes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirDTO);
        //TODO LO QUE SALE DE LA BASE SALE COMO ENTIDAD
        //TODO LO QUE ENTRA A LA BASE DEBE ENTRAR COMO ENTIDAD
    }

    private DTOClientes convertirDTO(ClientesEntity entity) {
        DTOClientes dto = new DTOClientes();
        dto.setClienteNit(entity.getClienteNit());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setCorreo(entity.getCorreo());
        dto.setTelefono(entity.getTelefono());
        dto.setCodEmpresa(entity.getCodEmpresa());

        if (entity.getUsuario() != null) {
            UsuarioEntity usuario = entity.getUsuario();
            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setUsuario(usuario.getUsuario());
            dto.setContrasenia(usuario.getContrasenia());
            if (usuario.getRol() != null) {
                dto.setIdRol(usuario.getRol().getIdRol());
                dto.setNombreRol(usuario.getRol().getRol());
            }
        }

        if (entity.getTipoCliente() != null){
            dto.setIdTipoCliente(entity.getTipoCliente().getIdTipoCliente());
            dto.setTipo(entity.getTipoCliente().getTipo());
        }
        return dto;
    }

    private boolean usuarioYaAsignado(Long idUsuario, String nitActual) {
        boolean usadoPorEmpleado = empleadosRepo.existsByUsuarioEmpleado_IdUsuario(idUsuario);
        boolean usadoPorTransportista = transportistasRepo.existsByUsuarioT_IdUsuario(idUsuario);
        boolean usadoPorOtroCliente = repo.existsByUsuario_IdUsuario(idUsuario)
                && !repo.existsByClienteNitAndUsuario_IdUsuario(nitActual, idUsuario);

        return usadoPorEmpleado || usadoPorTransportista || usadoPorOtroCliente;
    }

    public DTOClientes agregarCliente(DTOClientes dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un cliente vacío");
        }
        try {
            ClientesEntity entity = new ClientesEntity();
            entity.setClienteNit(dto.getClienteNit());
            entity.setNombre(dto.getNombre());
            entity.setApellido(dto.getApellido());
            entity.setCorreo(dto.getCorreo());
            entity.setTelefono(dto.getTelefono());
            entity.setCodEmpresa(dto.getCodEmpresa());

            if (dto.getIdUsuario() != null) {
                if (usuarioYaAsignado(dto.getIdUsuario(), dto.getClienteNit())) {
                    throw new ExceptionClienteUsuarioYaAsignado("Usuario ya está asignado a otro registro");
                }
                UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario())
                        .orElseThrow(() -> new ExceptionClienteUsuarioNoEncontrado(
                                "Usuario no encontrado con id: " + dto.getIdUsuario()));
                entity.setUsuario(usuario);
            }

            if(dto.getIdTipoCliente() != null){
                TipoClienteEntity tipoCliente = tipoClienteRepo.findById(dto.getIdTipoCliente())
                        .orElseThrow(() -> new ExceptionTipoClienteNoEncontrado(
                                "Tipo cliente no encontrado con id: " + dto.getIdTipoCliente()));
                entity.setTipoCliente(tipoCliente);
            }

            ClientesEntity guardado = repo.save(entity);
            return convertirDTO(guardado);

        }  catch (DataIntegrityViolationException e) {
            throw e; // lo manejará el ControllerAdvice
        }  catch (ExceptionClienteUsuarioYaAsignado |
                 ExceptionClienteUsuarioNoEncontrado e) {
            throw e;
        }
        catch (Exception e) {
            log.error("Error inesperado al agregar cliente", e);
            throw new ExceptionClienteNoRegistrado("Error al registrar cliente");
        }
    }


    public DTOClientes actualizarCliente(String nit, DTOClientes dto) {
        try {
            ClientesEntity entity = repo.findById(nit)
                    .orElseThrow(() -> new ExceptionClienteNoEncontrado("Cliente no encontrado con NIT: " + nit));

            entity.setNombre(dto.getNombre());
            entity.setApellido(dto.getApellido());
            entity.setCorreo(dto.getCorreo());
            entity.setTelefono(dto.getTelefono());
            entity.setCodEmpresa(dto.getCodEmpresa());

            if (dto.getIdUsuario() != null) {
                Long idActualUsuario = entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null;
                if (!dto.getIdUsuario().equals(idActualUsuario) && usuarioYaAsignado(dto.getIdUsuario(), nit)) {
                    throw new ExceptionClienteUsuarioYaAsignado("Usuario ya está asignado a otro registro");
                }

                UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario())
                        .orElseThrow(() -> new ExceptionClienteUsuarioNoEncontrado("Usuario no encontrado con id: " + dto.getIdUsuario()));
                entity.setUsuario(usuario);
            }

            if(dto.getIdTipoCliente() != null){
                TipoClienteEntity tipoCliente = tipoClienteRepo.findById(dto.getIdTipoCliente())
                        .orElseThrow(() -> new ExceptionTipoClienteNoEncontrado(
                                "Tipo cliente no encontrado con id: " + dto.getIdTipoCliente()));
                entity.setTipoCliente(tipoCliente);
            }
            return convertirDTO(repo.save(entity));

        } catch (DataIntegrityViolationException e) {
            throw e; // ⚡ deja que lo maneje el ControllerAdvice
        } catch (ExceptionClienteUsuarioYaAsignado | ExceptionClienteUsuarioNoEncontrado | ExceptionClienteNoEncontrado e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al actualizar cliente", e);
            throw new ExceptionClienteNoRegistrado("Error inesperado: cliente no actualizado");
        }
    }

    public DTOClientes patchCliente(String nit, DTOClientes dto) {
        try {
            ClientesEntity entity = repo.findById(nit)
                    .orElseThrow(() -> new ExceptionClienteNoEncontrado("Cliente no encontrado con NIT: " + nit));

            if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
            if (dto.getApellido() != null) entity.setApellido(dto.getApellido());
            if (dto.getCorreo() != null) entity.setCorreo(dto.getCorreo());
            if (dto.getTelefono() != null) entity.setTelefono(dto.getTelefono());
            if (dto.getCodEmpresa() != null) entity.setCodEmpresa(dto.getCodEmpresa());

            if (dto.getIdUsuario() != null) {
                Long idActualUsuario = entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null;
                if (!dto.getIdUsuario().equals(idActualUsuario) && usuarioYaAsignado(dto.getIdUsuario(), nit)) {
                    throw new ExceptionClienteUsuarioYaAsignado("Usuario ya está asignado a otro registro");
                }
                UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario())
                        .orElseThrow(() -> new ExceptionClienteUsuarioNoEncontrado("Usuario no encontrado con id: " + dto.getIdUsuario()));
                entity.setUsuario(usuario);
            }

            if(dto.getIdTipoCliente() != null){
                TipoClienteEntity tipoCliente = tipoClienteRepo.findById(dto.getIdTipoCliente())
                        .orElseThrow(() -> new ExceptionTipoClienteNoEncontrado(
                                "Tipo cliente no encontrado con id: " + dto.getIdTipoCliente()));
                entity.setTipoCliente(tipoCliente);
            }

            return convertirDTO(repo.save(entity));

        } catch (DataIntegrityViolationException e) {
            throw e; // deja que lo maneje el ControllerAdvice
        } catch (ExceptionClienteUsuarioYaAsignado | ExceptionClienteUsuarioNoEncontrado | ExceptionClienteNoEncontrado e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al actualizar parcialmente cliente", e);
            throw new ExceptionClienteNoRegistrado("Error inesperado: cliente no actualizado");
        }
    }


    public String eliminarCliente(String nit) {
        ClientesEntity entity = repo.findById(nit)
                .orElseThrow(() -> new ExceptionClienteNoEncontrado("Cliente no encontrado con NIT: " + nit));
        try {
            repo.delete(entity);
            return "Cliente eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionClienteNoRegistrado("No se pudo eliminar el cliente porque tiene registros relacionados");
        }
    }

    // Método para buscar cliente por NIT o por nombre
    public List<DTOClientes> buscarCliente(String nit, String nombre) {
        List<ClientesEntity> clientes;

        if (nit != null && !nit.isBlank()) {
            String nitLimpio = nit.trim(); // nueva variable
            ClientesEntity entity = repo.findById(nitLimpio)
                    .orElseThrow(() -> new ExceptionClienteNoEncontrado("No se encontró cliente con NIT: " + nitLimpio));
            clientes = List.of(entity);
        }
        else if (nombre != null && !nombre.isBlank()) {
            nombre = nombre.trim();
            clientes = repo.findByNombreIgnoreCaseContaining(nombre);
            if (clientes.isEmpty()) {
                throw new ExceptionClienteNoEncontrado("No se encontró cliente con nombre: " + nombre);
            }
        }
        else {
            throw new IllegalArgumentException("Debe proporcionar NIT o nombre para buscar");
        }

        return clientes.stream().map(this::convertirDTO).collect(Collectors.toList());
    }

}
