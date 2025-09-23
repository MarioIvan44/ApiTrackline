package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.RolesEntity;
import apiTrackline.proyectoPTC.Entities.TipoServicioEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTORoles;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoServicio;
import apiTrackline.proyectoPTC.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RolesService {
    @Autowired
    private RolesRepository repo;

    // Método público que usa el repositorio y convierte entidades a DTOs
    //Método HTTP GET (obtener datos)
    public List<DTORoles> obtenerRoles(){
        List<RolesEntity> roles = repo.findAll();
        List<DTORoles> collect = roles.stream()
                .map(this::convertirArolesDTO)
                .collect(Collectors.toList());
        return collect;
    }
    //Convertir a roles DTO
    private DTORoles convertirArolesDTO(RolesEntity rolesEntity){
        DTORoles dtoRoles = new DTORoles();
        dtoRoles.setIdRol(rolesEntity.getIdRol());
        dtoRoles.setRol(rolesEntity.getRol());
        return dtoRoles;
    }
}
