package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.InfoEmbarqueEntity;
import apiTrackline.proyectoPTC.Exceptions.InfoEmbarqueExceptions.ExceptionInfoEmbarqueNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.InfoEmbarqueExceptions.ExceptionInfoEmbarqueNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.InfoEmbarqueExceptions.ExceptionInfoEmbarqueRelacionado;
import apiTrackline.proyectoPTC.Models.DTO.DTOInfoEmbarque;
import apiTrackline.proyectoPTC.Repositories.InfoEmbarqueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InfoEmbarqueService {

    @Autowired
    private InfoEmbarqueRepository repo;

    // Método público que usa el repositorio y convierte entidades a DTOs
    //Método HTTP GET (obtener datos)
    public Page<DTOInfoEmbarque> obtenerInfoEmbarques(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InfoEmbarqueEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);

    }

    private DTOInfoEmbarque convertirADTO(InfoEmbarqueEntity entity) {
        DTOInfoEmbarque dto = new DTOInfoEmbarque();
        dto.setIdInfoEmbarque(entity.getIdInfoEmbarque());
        dto.setFacturas(entity.getFacturas());
        dto.setProveedorRef(entity.getProveedorRef());
        dto.setBultos(entity.getBultos());
        dto.setTipo(entity.getTipo());
        dto.setKilos(entity.getKilos());
        dto.setVolumen(entity.getVolumen());
        return dto;
    }

    //Método HTTP post (insertar información)
    public DTOInfoEmbarque agregarInfoEmbarque(DTOInfoEmbarque dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos.");
        }
        try {
            InfoEmbarqueEntity entity = new InfoEmbarqueEntity();
            entity.setFacturas(dto.getFacturas());
            entity.setProveedorRef(dto.getProveedorRef());
            entity.setBultos(dto.getBultos());
            entity.setTipo(dto.getTipo());
            entity.setKilos(dto.getKilos());
            entity.setVolumen(dto.getVolumen());

            InfoEmbarqueEntity creado = repo.save(entity);
            return convertirADTO(creado);
        } catch (Exception e) {
            log.error("Error al registrar la información del embarque: " + e.getMessage());
            throw new ExceptionInfoEmbarqueNoRegistrado("Error: Información de embarque no registrada");
        }
    }

    public String eliminarInfoEmbarque(Long id) {
        InfoEmbarqueEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionInfoEmbarqueNoEncontrado("Información de embarque no encontrada con id " + id));
        try {
            repo.delete(entity);
            return "Información de embarque eliminada correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionInfoEmbarqueRelacionado("No se pudo eliminar la información del embarque porque tiene registros relacionados");
        }
    }

    public DTOInfoEmbarque actualizarInfoEmbarque(Long id, DTOInfoEmbarque dto) {
        InfoEmbarqueEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionInfoEmbarqueNoEncontrado("Información de embarque no encontrada con id " + id));

        entity.setFacturas(dto.getFacturas());
        entity.setProveedorRef(dto.getProveedorRef());
        entity.setBultos(dto.getBultos());
        entity.setTipo(dto.getTipo());
        entity.setKilos(dto.getKilos());
        entity.setVolumen(dto.getVolumen());

        return convertirADTO(repo.save(entity));
    }


    public DTOInfoEmbarque patchInfoEmbarque(Long id, DTOInfoEmbarque dto) {
        InfoEmbarqueEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionInfoEmbarqueNoEncontrado("Información de embarque no encontrada con id " + id));

        if (dto.getFacturas() != null) entity.setFacturas(dto.getFacturas());
        if (dto.getProveedorRef() != null) entity.setProveedorRef(dto.getProveedorRef());
        if (dto.getBultos() != null) entity.setBultos(dto.getBultos());
        if (dto.getTipo() != null) entity.setTipo(dto.getTipo());
        if (dto.getKilos() != null) entity.setKilos(dto.getKilos());
        if (dto.getVolumen() != null) entity.setVolumen(dto.getVolumen());

        return convertirADTO(repo.save(entity));
    }

}
