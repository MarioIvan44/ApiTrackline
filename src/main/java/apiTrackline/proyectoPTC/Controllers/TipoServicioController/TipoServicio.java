package apiTrackline.proyectoPTC.Controllers.TipoServicioController;

import apiTrackline.proyectoPTC.Entities.TipoServicioEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoServicio;
import apiTrackline.proyectoPTC.Services.TipoServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiTipoServicio")
@CrossOrigin
public class TipoServicio {
    @Autowired //Inyecta la clase service que tiene la lógica de la empresa
    private TipoServicioService service;

    // Obtener todos los tipos de servicios
    // Ruta: GET localhost:8080/apiTipoServicio/data
    @GetMapping("/data")
    public List<DTOTipoServicio> getTipoServicio() {
        return service.obtenerTipoServicio();
    }
}