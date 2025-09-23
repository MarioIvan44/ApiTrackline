package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.ClientesEntity;
import apiTrackline.proyectoPTC.Entities.EmpleadosEntity;
import apiTrackline.proyectoPTC.Entities.TransportistaEntity;
import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOTransportista;
import apiTrackline.proyectoPTC.Repositories.ClientesRepository;
import apiTrackline.proyectoPTC.Repositories.EmpleadosRepository;
import apiTrackline.proyectoPTC.Repositories.TransportistaRepository;
import apiTrackline.proyectoPTC.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransportistaService {

    @Autowired
    private TransportistaRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private EmpleadosRepository empleadosRepo;

    @Autowired
    private ClientesRepository clientesRepo;

    // Obtener todos los transportistas y convertirlos a DTO
    public List<DTOTransportista> getData() {
        List<TransportistaEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private DTOTransportista convertirADTO(TransportistaEntity t) {
        DTOTransportista dto = new DTOTransportista();
        dto.setIdTransportista(t.getIdTransportista());
        dto.setNombre(t.getNombre());
        dto.setApellido(t.getApellido());
        dto.setTelefono(t.getTelefono());
        dto.setCorreo(t.getCorreo());
        dto.setNit(t.getNit());

        if (t.getUsuarioT() != null) {
            UsuarioEntity u = t.getUsuarioT();
            dto.setIdUsuario(u.getIdUsuario());
            dto.setNombreUsuario(u.getUsuario());
            if (u.getRol() != null) {
                dto.setIdRol(u.getRol().getIdRol());
                dto.setNombreRol(u.getRol().getRol());
            }
        }
        return dto;
    }

    private boolean usuarioYaAsignado(Long idUsuario, Long idTransportistaActual) {
        boolean usadoPorEmpleado = empleadosRepo.existsByUsuarioEmpleado_IdUsuario(idUsuario);
        boolean usadoPorCliente = clientesRepo.existsByUsuario_IdUsuario(idUsuario);
        boolean usadoPorOtroTransportista = repo.existsByUsuarioT_IdUsuarioAndIdTransportistaNot(idUsuario, idTransportistaActual);

        return usadoPorEmpleado || usadoPorCliente || usadoPorOtroTransportista;
    }

    // POST - Crear transportista
    public String post(DTOTransportista dto) {
        try {
            TransportistaEntity t = new TransportistaEntity();
            t.setNombre(dto.getNombre());
            t.setApellido(dto.getApellido());
            t.setTelefono(dto.getTelefono());
            t.setCorreo(dto.getCorreo());
            t.setNit(dto.getNit());

            if (dto.getIdUsuario() != null) {
                if (usuarioYaAsignado(dto.getIdUsuario(), -1L)) {
                    return "Error: El usuario ya está asignado a otro registro";
                }

                Optional<UsuarioEntity> usuario = usuarioRepo.findById(dto.getIdUsuario());
                if (usuario.isPresent()) {
                    t.setUsuarioT(usuario.get());
                } else {
                    return "Error: ID de usuario no encontrado";
                }
            }

            repo.save(t);
            return "Transportista creado correctamente";
        } catch (Exception e) {
            return "Error al crear el transportista: " + e.getMessage();
        }
    }

    // PUT - Actualización completa
    public String update(Long id, DTOTransportista dto) {
        Optional<TransportistaEntity> optional = repo.findById(id);
        if (optional.isPresent()) {
            TransportistaEntity t = optional.get();
            t.setNombre(dto.getNombre());
            t.setApellido(dto.getApellido());
            t.setTelefono(dto.getTelefono());
            t.setCorreo(dto.getCorreo());
            t.setNit(dto.getNit());

            if (dto.getIdUsuario() != null) {
                if (usuarioYaAsignado(dto.getIdUsuario(), id)) {
                    return "Error: El usuario ya está asignado a otro registro";
                }

                Optional<UsuarioEntity> usuario = usuarioRepo.findById(dto.getIdUsuario());
                if (usuario.isPresent()) {
                    t.setUsuarioT(usuario.get());
                } else {
                    return "Error: ID de usuario no encontrado";
                }
            }

            repo.save(t);
            return "Información del transportista actualizada correctamente";
        } else {
            return "Error: Transportista no encontrado";
        }
    }

    // PATCH - Actualización parcial
    public String patch(Long id, DTOTransportista dto) {
        Optional<TransportistaEntity> optional = repo.findById(id);
        if (optional.isPresent()) {
            TransportistaEntity t = optional.get();

            if (dto.getNombre() != null) t.setNombre(dto.getNombre());
            if (dto.getApellido() != null) t.setApellido(dto.getApellido());
            if (dto.getTelefono() != null) t.setTelefono(dto.getTelefono());
            if (dto.getCorreo() != null) t.setCorreo(dto.getCorreo());
            if (dto.getNit() != null) t.setNit(dto.getNit());

            if (dto.getIdUsuario() != null) {
                if (usuarioYaAsignado(dto.getIdUsuario(), id)) {
                    return "Error: El usuario ya está asignado a otro registro";
                }

                Optional<UsuarioEntity> usuario = usuarioRepo.findById(dto.getIdUsuario());
                if (usuario.isPresent()) {
                    t.setUsuarioT(usuario.get());
                } else {
                    return "Error: ID de usuario no encontrado";
                }
            }

            repo.save(t);
            return "Transportista actualizado parcialmente";
        } else {
            return "Error: Transportista no encontrado";
        }
    }

    // DELETE
    public String delete(Long id) {
        Optional<TransportistaEntity> optional = repo.findById(id);
        if (optional.isPresent()) {
            repo.deleteById(id);
            return "Transportista eliminado correctamente";
        } else {
            return "Transportista no encontrado";
        }
    }
}
