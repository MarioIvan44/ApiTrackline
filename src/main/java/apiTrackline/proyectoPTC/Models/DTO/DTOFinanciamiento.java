package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DTOFinanciamiento {

    private Long idFinanciamiento;

    @NotNull(message = "El ID del tipo de financiamiento es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El id tipo financiamiento no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnUpdate.class})
    private Long idTipoFinanciamiento;
    private String nombreTipoFinanciamiento;

    //Llave foranea Orden Servicio
    @Positive(message = "El id de orden servicio no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnUpdate.class})
    @NotNull(message = "El id de orden servicui es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    private Long idOrdenServicio;
    private String clienteNIT;

    @Positive(message = "La cantidad no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnUpdate.class})
    private Long cantidad;

    @Transient
    private BigDecimal total;

    @NotNull(message = "El monto es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "0.00", message = "El monto no puede ser negativo.", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Double monto;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}
