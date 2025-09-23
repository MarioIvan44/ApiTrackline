package apiTrackline.proyectoPTC.Exceptions.ValidacionesGlobales;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Esta anotación indica que esta clase manejará errores globalmente para todos los controladores.
public class ValidacionGlobalHandler {

    // Este método captura errores de validaciones como @NotBlank, @Size, @Pattern, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        // Recorre todos los errores de los campos del DTO y los agrega al mapa
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );

        // Devuelve un mapa con los nombres de campo y sus mensajes de error, con código 400 Bad Request
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    // Este metodo captura errores de formato en el cuerpo de la petición, como fechas mal escritas o tipos numéricos inválidos.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeLectura(HttpMessageNotReadableException ex) {
        Map<String, String> errores = new HashMap<>();

        // Verificamos si la causa fue un formato inválido (por ejemplo, fecha mal escrita)
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();

            // Recorremos los paths del error para identificar el campo que causó el error
            ife.getPath().forEach(ref -> {
                String campo = ref.getFieldName();
                Class<?> tipoEsperado = ife.getTargetType(); // Obtenemos el tipo de dato esperado
                String mensaje;

                // Si el tipo esperado era Date, damos un mensaje personalizado
                if (tipoEsperado == java.util.Date.class) {
                    mensaje = "El formato de fecha es inválido. Use 'dd-MM-yyyy'.";
                }
                // Si el tipo esperado era Boolean, damos un mensaje claro
                else if (tipoEsperado == Boolean.class || tipoEsperado == boolean.class) {
                    mensaje = "El campo '" + campo + "' solo acepta valores booleanos: true, false, 1 o 0.";
                }
                // Si no es Date ni Boolean, damos un mensaje genérico
                else {
                    mensaje = "El valor enviado no es válido para el campo: " + campo + ". Verifique los datos.";
                }

                errores.put(campo, mensaje);
            });
        } else {
            // Si no se sabe la causa exacta, mostramos un mensaje genérico
            errores.put("error", "Error en el formato de la solicitud. Verifique los datos enviados.");
        }

        // Devuelve los errores con código 400 Bad Request
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }


    // Este método captura violaciones de integridad de datos en Oracle (UNIQUE, FOREIGN KEY, NOT NULL, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> manejarErroresIntegridad(DataIntegrityViolationException ex) {
        Map<String, String> errores = new HashMap<>();
        String mensaje = ex.getMostSpecificCause().getMessage();

        if (mensaje != null && (mensaje.toUpperCase().contains("ORA-00001") || mensaje.toUpperCase().contains("UNIQUE"))) {
            errores.put("error", "Ya existe un registro con estos datos. Verifique que no haya valores repetidos en campos únicos.");
        }
        else if (mensaje != null && mensaje.toUpperCase().contains("ORA-02291")) {
            errores.put("error", "Violación de clave foránea: El registro relacionado no existe.");
        }
        else if (mensaje != null && mensaje.toUpperCase().contains("ORA-01400")) {
            errores.put("error", "No puede insertar un valor nulo en un campo obligatorio.");
        }
        else {
            errores.put("error", "Violación de integridad de datos. Verifique la información ingresada.");
        }

        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

}
