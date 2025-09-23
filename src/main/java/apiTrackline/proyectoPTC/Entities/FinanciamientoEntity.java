package apiTrackline.proyectoPTC.Entities;

import apiTrackline.proyectoPTC.Models.DTO.DTOFinanciamiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_FINANCIAMIENTOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FinanciamientoEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "financiamientoSeq")
    @SequenceGenerator(name = "financiamientoSeq", sequenceName = "SEQ_ID_FIN", allocationSize = 1)
    @Column(name = "IDFINANCIAMIENTO")
    private Long idFinanciamiento;

    @ManyToOne
    @JoinColumn(name = "IDTIPOFINANCIAMIENTO", referencedColumnName = "IDTIPOFINANCIAMIENTO")
    private TipoFinanciamientosEntity tipoFinanciamiento;

    @Column(name = "MONTO")
    private Double monto;

    @ManyToOne
    @JoinColumn(name = "IDORDENSERVICIO", referencedColumnName = "IDORDENSERVICIO")
    private OrdenServicioEntity ordenServicioFinanciamiento;

    @Column(name = "CANTIDAD")
    private Long cantidad;

    @Transient
    private BigDecimal total;
}