package apiTrackline.proyectoPTC.Controllers.RolesController;

import apiTrackline.proyectoPTC.Entities.RolesEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTORoles;
import apiTrackline.proyectoPTC.Services.RolesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiRoles")
@CrossOrigin
public class Roles {
    @Autowired
    private RolesService service;

    //Obtener todos los roles
    //GET: localhost:8080/apiRoles/getRoles
    @GetMapping("/getRoles")
    public List<DTORoles> getRoles(){
        return service.obtenerRoles();
    }
}
