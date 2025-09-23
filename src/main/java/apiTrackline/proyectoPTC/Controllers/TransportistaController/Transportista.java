package apiTrackline.proyectoPTC.Controllers.TransportistaController;

import apiTrackline.proyectoPTC.Models.DTO.DTOTransportista;
import apiTrackline.proyectoPTC.Services.TransportistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiTransportista")
@CrossOrigin
public class Transportista {
    @Autowired
    private TransportistaService service;

    // Obtener todos los transportistas
    //Ruta: GET localhost:8080/apiTransportista/dataTransportista
    @GetMapping("/dataTransportista")
    public List<DTOTransportista> getTransportistas() {
        return service.getData();
    }

    // POST - creación
    @PostMapping("/postTransportista")
    public String createTransportista(@Validated(DTOTransportista.OnCreate.class) @RequestBody DTOTransportista dto) {
        return service.post(dto);
    }

    // PUT - actualización total
    @PutMapping("/updateTransportista/{id}")
    public String updateTransportista(@PathVariable Long id, @Validated(DTOTransportista.OnUpdate.class) @RequestBody DTOTransportista dto) {
        return service.update(id, dto);
    }

    // PATCH - actualización parcial
    @PatchMapping("/updateTransportistaPartial/{id}")
    public String patchTransportista(@PathVariable Long id,
                                     @Validated(DTOTransportista.OnPatch.class) @RequestBody DTOTransportista dto) {
        return service.patch(id, dto);
    }

    //Elimina transportista
    //Ruta: DELETE localhost:8080/apiTransportista/deleteTransportista/{id}
    @DeleteMapping("/deleteTransportista/{id}")
    public String deleteTransportista(@PathVariable Long id) {
        return service.delete(id);
    }
}