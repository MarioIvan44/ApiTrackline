package apiTrackline.proyectoPTC.Entities;

import apiTrackline.proyectoPTC.Models.DTO.DTOCargos;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_CARGOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CargosEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargosSeq")
    @SequenceGenerator(name = "cargosSeq", sequenceName = "SEQ_ID_CARG", allocationSize = 1)
    @Column(name = "IDCARGOS")
    private Long idCargos;

    @ManyToOne
    @JoinColumn(name = "IDTIPODATOCONTABLE", referencedColumnName = "IDTIPODATOCONTABLE")
    private TipoDatoContableEntity tipoDatoContable;

    @ManyToOne
    @JoinColumn(name = "IDORDENSERVICIO", referencedColumnName = "IDORDENSERVICIO")
    private OrdenServicioEntity ordenServicioCargos;

    @Column(name = "MONTO")
    private Double monto;

    @Column(name = "CANTIDAD")
    private Long cantidad;

    @Transient
    private BigDecimal total;

}
