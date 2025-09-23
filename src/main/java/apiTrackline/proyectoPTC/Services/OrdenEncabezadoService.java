package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.OrdenEncabezadoEntity;
import apiTrackline.proyectoPTC.Exceptions.OrdenEncabezadoExceptions.*;
import apiTrackline.proyectoPTC.Models.DTO.DTOOrdenEncabezado;
import apiTrackline.proyectoPTC.Repositories.OrdenEncabezadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service
public class OrdenEncabezadoService {

    @Autowired
    private OrdenEncabezadoRepository repo;

    // Listar datos con paginación
    public Page<DTOOrdenEncabezado> getData(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable).map(this::convertirADTO);
    }

    // Buscar por ID
    public DTOOrdenEncabezado buscarPorId(Long id) {
        OrdenEncabezadoEntity orden = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenEncabezadoNoEncontrado(
                        "Orden encabezado no encontrado con id: " + id
                ));
        return convertirADTO(orden);
    }

    // Crear
    public DTOOrdenEncabezado post(DTOOrdenEncabezado dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos");
        }
        try {
            // Forzar la fecha a hoy, ignorando lo que venga en el JSON
            dto.setFecha(LocalDate.now(ZoneId.of("America/El_Salvador")));

            OrdenEncabezadoEntity orden = convertirAEntidad(dto);
            OrdenEncabezadoEntity creada = repo.save(orden);
            return convertirADTO(creada);
        } catch (DataIntegrityViolationException ex) {
            String msg = ex.getMostSpecificCause().getMessage();
            if (msg != null && (msg.contains("ORA-00001") || msg.toLowerCase().contains("unique"))) {
                throw new ExceptionOrdenEncabezadoNoRegistrado("Error: datos duplicados, verifique que no existan valores repetidos");
            }
            throw new ExceptionOrdenEncabezadoNoRegistrado("Error de integridad de datos: " + msg);
        } catch (Exception ex) {
            log.error("Error al registrar el orden encabezado", ex);
            throw new ExceptionOrdenEncabezadoNoRegistrado("Error: orden encabezado no registrado");
        }
    }

    // Actualizar total
    public DTOOrdenEncabezado update(Long id, DTOOrdenEncabezado dto) {
        OrdenEncabezadoEntity orden = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenEncabezadoNoEncontrado(
                        "Orden encabezado no encontrado con id: " + id
                ));
        try {
            //Forzamos que la fecha actualizada sea la misma (Fecha actual)
            orden.setFecha(LocalDate.now(ZoneId.of("America/El_Salvador")));

            orden.setEncargado(dto.getEncargado());
            orden.setReferencia(dto.getReferencia());
            orden.setImportador(dto.getImportador());
            orden.setNit(dto.getNIT());
            orden.setRegistroIva(dto.getRegistroIVA());
            orden.setFacturaA(dto.getFacturaA());
            orden.setEncargado2(dto.getEncargado2());
            orden.setNit2(dto.getNIT2());
            orden.setRegistroIva2(dto.getRegistroIVA2());
            return convertirADTO(repo.save(orden));
        } catch (DataIntegrityViolationException ex) {
            String msg = ex.getMostSpecificCause().getMessage();
            if (msg != null && (msg.contains("ORA-00001") || msg.toLowerCase().contains("unique"))) {
                throw new ExceptionOrdenEncabezadoNoRegistrado("Error: datos duplicados, verifique que no existan valores repetidos");
            }
            throw new ExceptionOrdenEncabezadoNoRegistrado("Error de integridad de datos: " + msg);
        }
    }

    // Actualización parcial (sin exigir campos NotNull)
    public DTOOrdenEncabezado patchOrden(Long id, DTOOrdenEncabezado dto) {
        OrdenEncabezadoEntity orden = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenEncabezadoNoEncontrado(
                        "Orden encabezado no encontrado con id: " + id
                ));

        // Solo actualiza si se envían valores
        if (dto.getFecha() != null) orden.setFecha(dto.getFecha());
        if (dto.getEncargado() != null) orden.setEncargado(dto.getEncargado());
        if (dto.getReferencia() != null) orden.setReferencia(dto.getReferencia());
        if (dto.getImportador() != null) orden.setImportador(dto.getImportador());
        if (dto.getNIT() != null) orden.setNit(dto.getNIT());
        if (dto.getRegistroIVA() != null) orden.setRegistroIva(dto.getRegistroIVA());
        if (dto.getFacturaA() != null) orden.setFacturaA(dto.getFacturaA());
        if (dto.getEncargado2() != null) orden.setEncargado2(dto.getEncargado2());
        if (dto.getNIT2() != null) orden.setNit2(dto.getNIT2());
        if (dto.getRegistroIVA2() != null) orden.setRegistroIva2(dto.getRegistroIVA2());

        try {
            return convertirADTO(repo.save(orden));
        } catch (DataIntegrityViolationException ex) {
            String msg = ex.getMostSpecificCause().getMessage();
            if (msg != null && (msg.contains("ORA-00001") || msg.toLowerCase().contains("unique"))) {
                throw new ExceptionOrdenEncabezadoNoRegistrado("Error: datos duplicados, verifique que no existan valores repetidos");
            }
            throw new ExceptionOrdenEncabezadoNoRegistrado("Error de integridad de datos: " + msg);
        }
    }

    // Eliminar
    public void delete(Long id) {
        OrdenEncabezadoEntity orden = repo.findById(id)
                .orElseThrow(() -> new ExceptionOrdenEncabezadoNoEncontrado(
                        "Orden encabezado no encontrado con id: " + id
                ));
        try {
            repo.delete(orden);
        } catch (DataIntegrityViolationException ex) {
            throw new ExceptionOrdenEncabezadoRelacionado("No se pudo eliminar porque tiene registros relacionados");
        }
    }

    // Conversión DTO → Entity
    private OrdenEncabezadoEntity convertirAEntidad(DTOOrdenEncabezado dto) {
        OrdenEncabezadoEntity orden = new OrdenEncabezadoEntity();
        orden.setFecha(dto.getFecha());
        orden.setEncargado(dto.getEncargado());
        orden.setReferencia(dto.getReferencia());
        orden.setImportador(dto.getImportador());
        orden.setNit(dto.getNIT());
        orden.setRegistroIva(dto.getRegistroIVA());
        orden.setFacturaA(dto.getFacturaA());
        orden.setEncargado2(dto.getEncargado2());
        orden.setNit2(dto.getNIT2());
        orden.setRegistroIva2(dto.getRegistroIVA2());
        return orden;
    }

    // Conversión Entity → DTO
    private DTOOrdenEncabezado convertirADTO(OrdenEncabezadoEntity orden) {
        DTOOrdenEncabezado dto = new DTOOrdenEncabezado();
        dto.setIdOrdenEncabezado(orden.getIdOrdenEncabezado());
        dto.setFecha(orden.getFecha());
        dto.setEncargado(orden.getEncargado());
        dto.setReferencia(orden.getReferencia());
        dto.setImportador(orden.getImportador());
        dto.setNIT(orden.getNit());
        dto.setRegistroIVA(orden.getRegistroIva());
        dto.setFacturaA(orden.getFacturaA());
        dto.setEncargado2(orden.getEncargado2());
        dto.setNIT2(orden.getNit2());
        dto.setRegistroIVA2(orden.getRegistroIva2());
        return dto;
    }
}
