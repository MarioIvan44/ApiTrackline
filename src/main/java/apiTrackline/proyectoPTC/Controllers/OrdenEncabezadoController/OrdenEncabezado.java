package apiTrackline.proyectoPTC.Controllers.OrdenEncabezadoController;

import apiTrackline.proyectoPTC.Exceptions.OrdenEncabezadoExceptions.ExceptionOrdenEncabezadoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.OrdenEncabezadoExceptions.ExceptionOrdenEncabezadoRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOOrdenEncabezado;
import apiTrackline.proyectoPTC.Services.OrdenEncabezadoService;
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
@RequestMapping("/apiOrden")  // Ruta base: localhost:8080/apiOrden
@CrossOrigin
public class OrdenEncabezado {
    @Autowired  // Inyecta el servicio que contiene la lógica de negocio
    private OrdenEncabezadoService service;

    //Obtener una órden encabezado por id
    //Ruta: GET localhost:8080/apiOrden/buscarPorId/id
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try{
            DTOOrdenEncabezado ordenEncabezado = service.buscarPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", ordenEncabezado
            ));
        } catch (ExceptionOrdenEncabezadoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Ocurrió un error no controlado"
            ));
        }
    }

    // Obtener todas las órdenes
    // Ruta: GET localhost:8080/apiOrden/dataOrden
    @GetMapping("/dataOrden")
    public ResponseEntity<?> getOrdenes(
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

        Page<DTOOrdenEncabezado> ordenEncabezados = service.getData(page, size);
        if (ordenEncabezados == null || ordenEncabezados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay orden encabezados registrados"
            ));
        }

        return ResponseEntity.ok(ordenEncabezados);
    }


    // Crear una nueva orden
    // Ruta: POST localhost:8080/apiOrden/postOrden
    @PostMapping("/postOrden")
    public ResponseEntity<?> postOrden(@Validated(DTOOrdenEncabezado.OnCreate.class) @RequestBody DTOOrdenEncabezado dto) {
        try{
            DTOOrdenEncabezado respuesta = service.post(dto);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Error al insertar datos",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Verfique los valores de los campos"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Orden encabezado creado correctamente"
            ));
        } catch (Exception e){
            log.error("Error inesperado al agregar la orden encabezado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Ocurrió un error no controlado"
            ));
        }
    }

    // Actualizar completamente una orden existente
    // Ruta: PUT localhost:8080/apiOrden/updateOrden/id
    @PutMapping("/updateOrden/{id}")
    public ResponseEntity<?> updateOrden(@PathVariable Long id, @Validated(DTOOrdenEncabezado.OnUpdate.class) @RequestBody DTOOrdenEncabezado dto) {
        try{
            DTOOrdenEncabezado ordenEncabezado = service.update(id, dto);
            return ResponseEntity.ok().body(Map.of(
                    "status", "Éxito",
                    "data", ordenEncabezado,
                    "message", "Orden Encabezado actualizado correctamente"
            ));
        } catch (ExceptionOrdenEncabezadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e){
            log.error("Error inesperado al actualizar el orden encabezado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado al actualizar el orden encabezado"
            ));
        }
    }

    // Actualizar parcialmente una orden existente
    // Ruta: PATCH localhost:8080/apiOrden/updateOrdenPartial/id
    @PatchMapping("/updateOrdenPartial/{id}")
    public ResponseEntity<?> patchOrden(@PathVariable Long id, @Validated(DTOOrdenEncabezado.OnPatch.class) @RequestBody DTOOrdenEncabezado dto) {
        try{
            DTOOrdenEncabezado ordenEncabezado = service.patchOrden(id, dto);
            return ResponseEntity.ok().body(Map.of(
                    "status", "Éxito",
                    "data", ordenEncabezado,
                    "message", "El orden encabezado ha sido actualizado parcialmente correctamente"
            ));
        } catch (ExceptionOrdenEncabezadoNoEncontrado noEncontrado){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", noEncontrado.getMessage()
            ));
        } catch (IllegalArgumentException illegal){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", illegal.getMessage()
            ));
        } catch (Exception e){
            log.error("Error inesperado al actualizar parcialmente la orden encabezado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado"
            ));
        }
    }

    // Eliminar una orden
    // Ruta: DELETE localhost:8080/apiOrden/deleteOrden/id
    @DeleteMapping("/deleteOrden/{id}")
    public ResponseEntity<?> deleteOrden(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().body(Map.of(
                    "status", "Éxito",
                    "message", "Orden encabezado eliminado correctamente"
            ));
        } catch (ExceptionOrdenEncabezadoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionOrdenEncabezadoRelacionado e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "No se pudo eliminar el orden encabezado porque tiene registros relacionados"
            ));
        } catch (Exception e){
            log.error("Error inesperado al eliminar la orden encabezado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar el orden encabezado"
            ));
        }
    }
}
