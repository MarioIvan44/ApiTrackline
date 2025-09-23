package apiTrackline.proyectoPTC.Controllers.EmpleadosController;

import apiTrackline.proyectoPTC.Exceptions.EmpleadosExceptions.*;
import apiTrackline.proyectoPTC.Models.DTO.DTOEmpleados;
import apiTrackline.proyectoPTC.Models.DTO.DTOViaje;
import apiTrackline.proyectoPTC.Services.EmpleadosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/apiEmpleados")
public class Empleados {

    @Autowired
    private EmpleadosService service;

    // MÉTODO GET POR ID
    // RUTA: localhost:8080/apiEmpleados/obtenerEmpleadoPorId/{id}
    @GetMapping("/obtenerEmpleadoPorId/{id}")
    public ResponseEntity<?> obtenerEmpleadoPorId(@PathVariable Long id) {
        try {
            DTOEmpleados empleado = service.buscarEmpleadoPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", empleado
            ));
        } catch (ExceptionEmpleadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al buscar un empleado por id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar empleado por ID"
            ));
        }
    }


    // GET con paginación
    @GetMapping("/datosEmpleados")
    public ResponseEntity<?> obtenerEmpleados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            Page<DTOEmpleados> empleados = service.obtenerEmpleados(page, size);
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            log.error("Error al obtener empleados", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al obtener empleados"
            ));
        }
    }

    //GET SIN PAGINACION
    @GetMapping("/obtenerEmpleados")
    public ResponseEntity<?> empleados() {
        try {
            List<DTOEmpleados> empleados = service.getSinPaginacion();
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            log.error("Error al obtener empleados", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al obtener empleados"
            ));
        }
    }


    // MÉTODO POST
    // RUTA: localhost:8080/apiEmpleados/agregarEmpleado
    @PostMapping("/agregarEmpleado")
    public ResponseEntity<?> agregarEmpleado(@Validated(DTOEmpleados.OnCreate.class) @RequestBody DTOEmpleados dto) {
        try {
            DTOEmpleados respuesta = service.agregarEmpleado(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Empleado creado correctamente"
            ));
        } catch (ExceptionEmpleadoUsuarioYaAsignado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionEmpleadoUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionEmpleadoNoRegistrado e) {
            log.error("Error inesperado al agregar el empleado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error inesperado al registrar el empleado"
            ));
        }
    }

    // MÉTODO PUT
    // RUTA: localhost:8080/apiEmpleados/actualizarEmpleado/{id}
    @PutMapping("/actualizarEmpleado/{id}")
    public ResponseEntity<?> actualizarEmpleado(@PathVariable Long id, @Validated(DTOEmpleados.OnUpdate.class) @RequestBody DTOEmpleados dto) {
        try {
            DTOEmpleados actualizado = service.actualizarEmpleado(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionEmpleadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionEmpleadoUsuarioYaAsignado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionEmpleadoUsuarioNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }

    // MÉTODO PATCH
    // RUTA: localhost:8080/apiEmpleados/actualizarParcialmente/{id}
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> patchEmpleado(@PathVariable Long id, @Validated(DTOEmpleados.OnPatch.class) @RequestBody DTOEmpleados dto) {
        try {
            DTOEmpleados actualizado = service.patchEmpleado(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionEmpleadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionEmpleadoUsuarioYaAsignado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionEmpleadoUsuarioNoEncontrado e) {
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
            log.error("Error inesperado al actualizar parcialmente el empleado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al editar parcialmente empleado"
            ));
        }
    }

    // MÉTODO DELETE
    // RUTA: localhost:8080/apiEmpleados/eliminarEmpleado/{id}
    @DeleteMapping("/eliminarEmpleado/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Long id) {
        try {
            service.eliminarEmpleado(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "Empleado eliminado correctamente"
            ));
        } catch (ExceptionEmpleadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionEmpleadoRelacionado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "El empleado no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e) {
            log.error("Error inesperado al elimnar el empleado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al eliminar empleado"
            ));
        }
    }
}
