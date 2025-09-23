package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DTOCargos {

    private Long idCargos;

    //Llave foranea TipoDatoContable
    @NotNull(message = "El ID del tipo de dato contable es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El ID de tipo de datos contables no puede ser negativa", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idTipoDatoContable;
    private String nombreTipoDatoContable;

    //Llave foranea Orden Servicio
    @Positive(message = "El ID de orden servicio no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idOrdenServicio;
    private String clienteNit;

    @Positive(message = "La cantidad no puede ser negativa", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long cantidad;


    @NotNull(message = "El monto es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "0.00", message = "El monto no puede ser negativo.", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Double monto;

    @Transient
    private BigDecimal total;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}

