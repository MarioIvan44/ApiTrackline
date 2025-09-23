package apiTrackline.proyectoPTC.Controllers.ClientesController;

import apiTrackline.proyectoPTC.Exceptions.ClientesExceptions.ExceptionClienteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ClientesExceptions.ExceptionClienteNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.ClientesExceptions.ExceptionClienteUsuarioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ClientesExceptions.ExceptionClienteUsuarioYaAsignado;
import apiTrackline.proyectoPTC.Models.DTO.DTOClientes;
import apiTrackline.proyectoPTC.Services.ClientesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/apiClientes")
@CrossOrigin(origins = "http://localhost")
public class Clientes {
    @Autowired
    private ClientesService service;

    // GET con paginación
    @GetMapping("/datosClientes")
    public ResponseEntity<?> obtenerClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            Page<DTOClientes> clientes = service.obtenerClientes(page, size);
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            log.error("Error al obtener clientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al obtener clientes"
            ));
        }
    }

    // GET por NIT o nombre
    @GetMapping("/buscarCliente")
    public ResponseEntity<?> buscarCliente(
            @RequestParam(required = false) String nit,
            @RequestParam(required = false) String nombre
    ) {
        try {
            List<DTOClientes> clientes = service.buscarCliente(nit, nombre);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", clientes
            ));
        } catch (ExceptionClienteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al buscar cliente", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar cliente"
            ));
        }
    }

    @GetMapping("/clientes")
    public ResponseEntity<?> obtenerClientes(){
        try {
            List<DTOClientes> clientes = service.obtenerSinPaginacion();
            return ResponseEntity.ok(clientes);
        }
        catch (Exception e) {
            log.error("Error al obtener clientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al obtener clientes"
            ));
        }
    }
    // POST
    @PostMapping("/agregarCliente")
    public ResponseEntity<?> agregarCliente(@Validated(DTOClientes.OnCreate.class) @RequestBody DTOClientes dto) {
        try {
            DTOClientes creado = service.agregarCliente(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", creado,
                    "message", "Cliente creado correctamente"
            ));
        } catch (ExceptionClienteUsuarioYaAsignado | ExceptionClienteUsuarioNoEncontrado |
                 ExceptionClienteNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // PUT
    @PutMapping("/actualizarCliente/{nit}")
    public ResponseEntity<?> actualizarCliente(@PathVariable String nit, @Validated(DTOClientes.OnUpdate.class) @RequestBody DTOClientes dto) {
        try {
            DTOClientes actualizado = service.actualizarCliente(nit, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionClienteUsuarioYaAsignado | ExceptionClienteUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionClienteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // PATCH
    @PatchMapping("/actualizarParcialmente/{nit}")
    public ResponseEntity<?> patchCliente(@PathVariable String nit, @Validated(DTOClientes.OnPatch.class) @RequestBody DTOClientes dto) {
        try {
            DTOClientes actualizado = service.patchCliente(nit, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionClienteUsuarioYaAsignado | ExceptionClienteUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionClienteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // DELETE
    @DeleteMapping("/eliminarCliente/{nit}")
    public ResponseEntity<?> eliminarCliente(@PathVariable String nit) {
        try {
            String msg = service.eliminarCliente(nit);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", msg
            ));
        } catch (ExceptionClienteNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionClienteNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar cliente", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al eliminar cliente"
            ));
        }
    }
}
