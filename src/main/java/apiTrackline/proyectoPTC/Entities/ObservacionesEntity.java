package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_OBSERVACIONES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ObservacionesEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "observacionesSeq")
    @SequenceGenerator(name = "observacionesSeq", sequenceName = "SEQ_ID_OBS", allocationSize = 1)
    @Column(name = "IDOBSERVACIONES")
    private Long idObservaciones;

    @ManyToOne
    @JoinColumn(name = "IDSELECTIVO", referencedColumnName = "IDSELECTIVO")
    private SelectivoEntity idSelectivo;

    @Column(name = "OBSERVACIONES")
    private String observaciones;
}
