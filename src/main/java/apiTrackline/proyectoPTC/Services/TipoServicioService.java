package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.TipoServicioEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoServicio;
import apiTrackline.proyectoPTC.Repositories.TipoServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TipoServicioService {
    @Autowired
    TipoServicioRepository repo;

    // Método público que usa el repositorio y convierte entidades a DTOs
    //Método HTTP GET (obtener datos)
    public List<DTOTipoServicio> obtenerTipoServicio() {
        List<TipoServicioEntity> servicios = repo.findAll();
        return servicios.stream()
                .map(this::convertirATipoServicioDTO)
                .collect(Collectors.toList());
    }

    //Convierte los datos del usuario a DTO
    private DTOTipoServicio convertirATipoServicioDTO(TipoServicioEntity servicio){
        DTOTipoServicio dto = new DTOTipoServicio();
        dto.setIdTipoServicio(servicio.getIdTipoServicio());
        dto.setTipoServicio(servicio.getTipoServicio());
        return dto;
    }
}
