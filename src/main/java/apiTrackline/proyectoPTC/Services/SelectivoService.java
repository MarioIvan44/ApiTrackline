package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.AduanaEntity;
import apiTrackline.proyectoPTC.Entities.SelectivoEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOAduana;
import apiTrackline.proyectoPTC.Models.DTO.DTOSelectivo;
import apiTrackline.proyectoPTC.Repositories.SelectivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelectivoService {
    @Autowired
    private SelectivoRepository repo;

    public Page<DTOSelectivo> obtenerSelectivos(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<SelectivoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTOselectivo);
        //TODO LO QUE SALE DE LA BASE SALE COMO ENTIDAD
        //TODO LO QUE ENTRA A LA BASE DEBE ENTRAR COMO ENTIDAD
    }

    private DTOSelectivo convertirADTOselectivo(SelectivoEntity entity){
        DTOSelectivo dto = new DTOSelectivo();
        dto.setIdSelectivo(entity.getIdSelectivo());
        dto.setColorSelectivo(entity.getColorSelectivo());
        return dto;
    }
}