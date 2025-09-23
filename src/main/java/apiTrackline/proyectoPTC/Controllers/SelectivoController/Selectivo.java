package apiTrackline.proyectoPTC.Controllers.SelectivoController;

import apiTrackline.proyectoPTC.Models.DTO.DTORecoleccion;
import apiTrackline.proyectoPTC.Models.DTO.DTOSelectivo;
import apiTrackline.proyectoPTC.Services.SelectivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/apiSelectivo")
@CrossOrigin
public class Selectivo{
    @Autowired
    private SelectivoService service;

    // MÉTODO GET (PAGINADO)
    // RUTA: localhost:8080/apiSelectivo/obtenerSelectivos
    @GetMapping("/obtenerSeletivos")
    public ResponseEntity<?> obtenerSelectivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (page < 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "message", "El número de página no puede ser negativo"
            ));
        }

        if (size <= 0 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }

        Page<DTOSelectivo> selectivos = service.obtenerSelectivos(page, size);
        if (selectivos == null || selectivos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay registros de recolección"
            ));
        }
        return ResponseEntity.ok(selectivos);
    }

}