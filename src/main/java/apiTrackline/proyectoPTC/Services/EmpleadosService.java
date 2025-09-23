package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.EmpleadosEntity;
import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Exceptions.EmpleadosExceptions.*;
import apiTrackline.proyectoPTC.Models.DTO.DTOEmpleados;
import apiTrackline.proyectoPTC.Repositories.ClientesRepository;
import apiTrackline.proyectoPTC.Repositories.EmpleadosRepository;
import apiTrackline.proyectoPTC.Repositories.TransportistaRepository;
import apiTrackline.proyectoPTC.Repositories.UsuarioRepository;
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
public class EmpleadosService {

    @Autowired
    private EmpleadosRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ClientesRepository clientesRepo;

    @Autowired
    private TransportistaRepository transportistasRepo;

    public List<DTOEmpleados> getSinPaginacion() {
        List<EmpleadosEntity> empleados = repo.findAll();
        return empleados.stream()
                .map(this::convertirAEmpleadosDTO)
                .collect(Collectors.toList());
    }

    public Page<DTOEmpleados> obtenerEmpleados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmpleadosEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirAEmpleadosDTO);
    }

    private DTOEmpleados convertirAEmpleadosDTO(EmpleadosEntity entity) {
        DTOEmpleados dto = new DTOEmpleados();
        dto.setIdEmpleado(entity.getIdEmpleado());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setTelefono(entity.getTelefono());
        dto.setCorreo(entity.getCorreo());
        dto.setNit(entity.getNit());

        if (entity.getUsuarioEmpleado() != null) {
            UsuarioEntity usuario = entity.getUsuarioEmpleado();
            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setUsuario(usuario.getUsuario());
            dto.setContrasenia(usuario.getContrasenia());
            if (usuario.getRol() != null) {
                dto.setIdRol(usuario.getRol().getIdRol());
                dto.setNombreRol(usuario.getRol().getRol());
            }
        }
        return dto;
    }

    private boolean usuarioYaAsignado(Long idUsuario, Long idEmpleadoActual) {
        boolean usadoPorCliente = clientesRepo.existsByUsuario_IdUsuario(idUsuario);
        boolean usadoPorTransportista = transportistasRepo.existsByUsuarioT_IdUsuario(idUsuario);
        boolean usadoPorOtroEmpleado = repo.existsByUsuarioEmpleado_IdUsuarioAndIdEmpleadoNot(idUsuario, idEmpleadoActual);

        return usadoPorCliente || usadoPorTransportista || usadoPorOtroEmpleado;
    }

    public DTOEmpleados agregarEmpleado(DTOEmpleados dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos");
        }

        try {
            EmpleadosEntity entity = new EmpleadosEntity();
            entity.setNombre(dto.getNombre());
            entity.setApellido(dto.getApellido());
            entity.setTelefono(dto.getTelefono());
            entity.setCorreo(dto.getCorreo());
            entity.setNit(dto.getNit());

            if (dto.getIdUsuario() != null) {
                if (usuarioYaAsignado(dto.getIdUsuario(), -1L)) {
                    throw new ExceptionEmpleadoUsuarioYaAsignado(
                            "El usuario con ID " + dto.getIdUsuario() + " ya está asignado a otro registro"
                    );
                }

                UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario())
                        .orElseThrow(() -> new ExceptionEmpleadoUsuarioNoEncontrado(
                                "Usuario no encontrado con ID " + dto.getIdUsuario()
                        ));

                entity.setUsuarioEmpleado(usuario);
            }

            EmpleadosEntity empleadoCreado = repo.save(entity);
            return convertirAEmpleadosDTO(empleadoCreado);

        } catch (DataIntegrityViolationException e) {
            throw e; // lo maneja ControllerAdvice
        } catch (ExceptionEmpleadoUsuarioYaAsignado | ExceptionEmpleadoUsuarioNoEncontrado e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al agregar el empleado: ", e);
            throw new ExceptionEmpleadoNoRegistrado("Error inesperado: empleado no registrado");
        }
    }

    public DTOEmpleados actualizarEmpleado(Long id, DTOEmpleados dto) {
        try {
            EmpleadosEntity empleado = repo.findById(id)
                    .orElseThrow(() -> new ExceptionEmpleadoNoEncontrado("Empleado no encontrado con id " + id));

            empleado.setNombre(dto.getNombre());
            empleado.setApellido(dto.getApellido());
            empleado.setTelefono(dto.getTelefono());
            empleado.setCorreo(dto.getCorreo());
            empleado.setNit(dto.getNit());

            if (dto.getIdUsuario() != null) {
                if (usuarioYaAsignado(dto.getIdUsuario(), id)) {
                    throw new ExceptionEmpleadoUsuarioYaAsignado(
                            "El usuario con ID " + dto.getIdUsuario() + " ya está asignado a otro registro"
                    );
                }

                UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario())
                        .orElseThrow(() -> new ExceptionEmpleadoUsuarioNoEncontrado(
                                "Usuario no encontrado con ID " + dto.getIdUsuario()
                        ));

                empleado.setUsuarioEmpleado(usuario);
            }

            return convertirAEmpleadosDTO(repo.save(empleado));

        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (ExceptionEmpleadoUsuarioYaAsignado | ExceptionEmpleadoUsuarioNoEncontrado | ExceptionEmpleadoNoEncontrado e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al actualizar el empleado: ", e);
            throw new ExceptionEmpleadoNoRegistrado("Error inesperado: empleado no actualizado");
        }
    }

    public DTOEmpleados patchEmpleado(Long id, DTOEmpleados dto) {
        try {
            EmpleadosEntity empleado = repo.findById(id)
                    .orElseThrow(() -> new ExceptionEmpleadoNoEncontrado("Empleado no encontrado con id " + id));

            if (dto.getNombre() != null) {
                if (dto.getNombre().isBlank()) {
                    throw new IllegalArgumentException("El nombre no puede estar en blanco");
                }
                empleado.setNombre(dto.getNombre());
            }
            if (dto.getApellido() != null) {
                empleado.setApellido(dto.getApellido());
            }
            if (dto.getTelefono() != null) empleado.setTelefono(dto.getTelefono());
            if (dto.getCorreo() != null) empleado.setCorreo(dto.getCorreo());
            if (dto.getNit() != null) empleado.setNit(dto.getNit());

            if (dto.getIdUsuario() != null) {
                if (usuarioYaAsignado(dto.getIdUsuario(), id)) {
                    throw new ExceptionEmpleadoUsuarioYaAsignado(
                            "El usuario con ID " + dto.getIdUsuario() + " ya está asignado a otro registro"
                    );
                }

                UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario())
                        .orElseThrow(() -> new ExceptionEmpleadoUsuarioNoEncontrado(
                                "Usuario no encontrado con ID " + dto.getIdUsuario()
                        ));

                empleado.setUsuarioEmpleado(usuario);
            }

            return convertirAEmpleadosDTO(repo.save(empleado));

        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (ExceptionEmpleadoUsuarioYaAsignado | ExceptionEmpleadoUsuarioNoEncontrado | ExceptionEmpleadoNoEncontrado e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al actualizar parcialmente el empleado: ", e);
            throw new ExceptionEmpleadoNoRegistrado("Error inesperado: empleado no actualizado");
        }
    }

    public String eliminarEmpleado(Long id) {
        EmpleadosEntity empleado = repo.findById(id)
                .orElseThrow(() -> new ExceptionEmpleadoNoEncontrado("Empleado no encontrado con id: " + id));
        try {
            repo.delete(empleado);
            return "Empleado eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw e; // lo maneja ControllerAdvice
        } catch (Exception e) {
            log.error("Error inesperado al eliminar el empleado: ", e);
            throw new ExceptionEmpleadoNoRegistrado("Error inesperado: empleado no eliminado");
        }
    }

    public DTOEmpleados buscarEmpleadoPorId(Long id) {
        EmpleadosEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionEmpleadoNoEncontrado("No se encontró el empleado con ID: " + id));
        return convertirAEmpleadosDTO(entity);
    }
}
