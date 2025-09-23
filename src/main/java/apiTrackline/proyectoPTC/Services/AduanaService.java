package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.AduanaEntity;
import apiTrackline.proyectoPTC.Entities.TipoServicioEntity;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaNoEncontrada;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaNoRegistrada;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaRelacionada;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionTipoServicioNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOAduana;
import apiTrackline.proyectoPTC.Repositories.AduanaRepository;
import apiTrackline.proyectoPTC.Repositories.TipoServicioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AduanaService {
    @Autowired
    private AduanaRepository repo;

    @Autowired
    private TipoServicioRepository tipoServiciorepo;

    public Page<DTOAduana> obtenerAduanas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<AduanaEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirAAduanaDTO);
        //TODO LO QUE SALE DE LA BASE SALE COMO ENTIDAD
        //TODO LO QUE ENTRA A LA BASE DEBE ENTRAR COMO ENTIDAD
    }

    // Método que convierte una entidad AduanaEntity a un objeto DTOAduana
    public DTOAduana convertirAAduanaDTO(AduanaEntity aduanaEntity) {
        // Se crea un nuevo objeto DTO (Data Transfer Object) para transportar los datos
        DTOAduana dto = new DTOAduana();
        // Se asigna el ID de la aduana directamente desde la entidad
        dto.setIdAduana(aduanaEntity.getIdAduana());
        // Verificamos si el objeto relacionado "TipoServicio" no es nulo (evitamos NullPointerException)
        if (aduanaEntity.getTipoServicio() != null) {
            // Se obtiene el ID del tipo de servicio relacionado
            dto.setIdTipoServicio(aduanaEntity.getTipoServicio().getIdTipoServicio());
            // También se obtiene el nombre del tipo de servicio
            dto.setNombreTipoServicio(aduanaEntity.getTipoServicio().getTipoServicio());
        } else {
            // Si es nulo, se dejan en null ambos campos
            dto.setIdTipoServicio(null);
            dto.setNombreTipoServicio(null);
        }

        // Se copian los demás atributos directamente desde la entidad
        dto.setDM(aduanaEntity.getDM());
        dto.setPrimeraModalidad(aduanaEntity.getPrimeraModalidad());
        dto.setSegundaModalidad(aduanaEntity.getSegundaModalidad());
        dto.setDigitador(aduanaEntity.getDigitador());
        dto.setTramitador(aduanaEntity.getTramitador());

        // Se retorna el DTO ya completo
        return dto;
    }

    public DTOAduana agregarAduana(DTOAduana json) {
        if (json == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos");
        }

        // Buscar el tipo de servicio por el ID que viene en el DTO
        TipoServicioEntity tipoServicio = tipoServiciorepo.findById(json.getIdTipoServicio())
                .orElseThrow(() -> new ExceptionTipoServicioNoEncontrado("Tipo servicio no encontrado con id " + json.getIdTipoServicio()));

        try {
            AduanaEntity entity = new AduanaEntity();
            entity.setDM(json.getDM());
            entity.setPrimeraModalidad(json.getPrimeraModalidad());
            entity.setSegundaModalidad(json.getSegundaModalidad());
            entity.setDigitador(json.getDigitador());
            entity.setTramitador(json.getTramitador());
            entity.setTipoServicio(tipoServicio);

            AduanaEntity aduanaCreada = repo.save(entity);
            return convertirAAduanaDTO(aduanaCreada);

        } catch (Exception e) {
            log.error("Error al registrar la aduana: " + e);
            throw new ExceptionAduanaNoRegistrada("Error: aduana no registrada");
        }
    }


    public DTOAduana actualizarAduana(Long id, DTOAduana dto) {
        AduanaEntity aduana = repo.findById(id)
                .orElseThrow(() -> new ExceptionAduanaNoEncontrada("Aduana no encontrada con id " + id));

        aduana.setDM(dto.getDM());
        aduana.setPrimeraModalidad(dto.getPrimeraModalidad());
        aduana.setSegundaModalidad(dto.getSegundaModalidad());
        aduana.setDigitador(dto.getDigitador());
        aduana.setTramitador(dto.getTramitador());

        if (dto.getIdTipoServicio() != null) {
            TipoServicioEntity tipoServicio = tipoServiciorepo.findById(dto.getIdTipoServicio())
                    .orElseThrow(() -> new ExceptionTipoServicioNoEncontrado("Tipo servicio no encontrado con id " + dto.getIdTipoServicio()));
            aduana.setTipoServicio(tipoServicio);
        }
        return convertirAAduanaDTO(repo.save(aduana));
    }


    public DTOAduana patchAduana(Long id, DTOAduana json) {
        AduanaEntity aduana = repo.findById(id)
                .orElseThrow(() -> new ExceptionAduanaNoEncontrada("Aduana no encontrada con id " + id));

        //Este campo tiene Not null en la BD, por ende, tiene anotación @Not Blank en el DTO
        //Si el usuario quiere editar el DM con "" o con null
        //ESTO SOLO SE HACE SI EL CAMPO TIENE @NOT BLANK EN EL DTO
        if (json.getDM() != null) { //Si no es nulo, es decir, si se está actualizando ese campo en el body del Patch
            if (json.getDM().isBlank()) { //Si al momento de actualizarlo el usuario envia un "", null, o " "
                throw new IllegalArgumentException("El DM no puede estar en blanco, debe contener datos"); //Se activa la excepción
            }
            //Si el usuario envia una cadena correcta, se actualiza correctamente
            aduana.setDM(json.getDM());
        }
        if (json.getPrimeraModalidad() != null) aduana.setPrimeraModalidad(json.getPrimeraModalidad());
        if (json.getSegundaModalidad() != null) aduana.setSegundaModalidad(json.getSegundaModalidad());
        if (json.getDigitador() != null) {
            if(json.getDigitador().isBlank()){
                throw new IllegalArgumentException("El digitador no puede estar en blanco, debe contener datos");
            }
            aduana.setDigitador(json.getDigitador());
        }
        if (json.getTramitador() != null) aduana.setTramitador(json.getTramitador());

        if (json.getIdTipoServicio() != null) {
            TipoServicioEntity tipoServicio = tipoServiciorepo.findById(json.getIdTipoServicio())
                    .orElseThrow(() -> new ExceptionTipoServicioNoEncontrado(
                            "Tipo servicio no encontrado con id " + json.getIdTipoServicio()));
            aduana.setTipoServicio(tipoServicio);
        }
        return convertirAAduanaDTO(repo.save(aduana));
    }


    //ELiminar
    public String eliminarAduana(Long id) {
        AduanaEntity aduana = repo.findById(id)
                .orElseThrow(() -> new ExceptionAduanaNoEncontrada("Aduana no encontrada con id " + id));
        try {
            repo.delete(aduana);
            return "Aduana eliminada correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionAduanaRelacionada("No se pudo eliminar la aduana porque tiene registros relacionados");
        }
    }

    public DTOAduana buscarAduanaPorId(Long id) {
        AduanaEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionAduanaNoEncontrada("No se encontró la aduana con ID: " + id));
        return convertirAAduanaDTO(entity);
    }
}
