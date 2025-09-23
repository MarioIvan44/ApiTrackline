package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOEstados {
    private Long idEstado;

    private Boolean documentos;
    private Boolean clasificacion;
    private Boolean digitacion;
    private Boolean registro;
    private Boolean pago;

    @Positive(message = "El idSelectivo no puede ser negativo", groups = {OnCreate.class, OnPatch.class, OnUpdate.class})
    private Long idSelectivo;
    private String colorSelectivo;

    private Boolean levantePago;
    private Boolean equipoTransporte;
    private Boolean carga;
    private Boolean enCamino;
    private Boolean entregada;
    private Boolean facturacion;

    public interface OnCreate{}
    public interface OnUpdate{}
    public interface OnPatch{}



}
