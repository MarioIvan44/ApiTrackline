package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TB_TIPODATOSCONTABLES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TipoDatoContableEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoDatoContableSeq")
    @SequenceGenerator(name = "tipoDatoContableSeq", sequenceName = "SEQ_ID_TDC", allocationSize = 1)
    @Column(name = "IDTIPODATOCONTABLE")
    private Long idTipoDatoContable;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "PRECIOUNI")
    private Double precioUni;
}