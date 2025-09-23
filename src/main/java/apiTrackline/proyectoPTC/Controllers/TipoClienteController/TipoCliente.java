package apiTrackline.proyectoPTC.Controllers.TipoClienteController;

import apiTrackline.proyectoPTC.Models.DTO.DTOTipoCliente;
import apiTrackline.proyectoPTC.Services.TipoClienteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/apiTipoCliente")
@CrossOrigin
public class TipoCliente {
    @Autowired
    private TipoClienteService service;

    @GetMapping("/obtenerTodos")
    public ResponseEntity<?> getSinPaginacion(){
        try {
            List<DTOTipoCliente> tiposClientes = service.obtenerTodos();
            return ResponseEntity.ok(tiposClientes);
        }catch (Exception e){
            log.error("Error al obtener tipos clientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al obtener los tipos de clientes"
            ));
        }
    }

}
