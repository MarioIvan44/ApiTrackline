package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.TipoClienteEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOEmpleados;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoCliente;
import apiTrackline.proyectoPTC.Repositories.TipoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoClienteService {

    @Autowired
    private TipoClienteRepository repo;

    public List<DTOTipoCliente> obtenerTodos(){
        List<TipoClienteEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private DTOTipoCliente convertirADTO(TipoClienteEntity entity){
        DTOTipoCliente dto = new DTOTipoCliente();
        dto.setIdTipoCliente(entity.getIdTipoCliente());
        dto.setTipo(entity.getTipo());
        return dto;
    }
}
