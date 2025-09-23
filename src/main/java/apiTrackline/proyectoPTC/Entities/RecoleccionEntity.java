package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TB_RECOLECCION")
public class RecoleccionEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recoleccionSeq")
    @SequenceGenerator(name = "recoleccionSeq", sequenceName = "SEQ_ID_RECO", allocationSize = 1)
    @Column(name = "IDRECOLECCION")
    private Long idRecoleccion;

    @Column(name = "TRANSPORTE")
    private Boolean transporte;

    @Column(name = "RECOLECCIONENTREGA")
    private Boolean recoleccionEntrega;

    @Column(name = "NUMERODOC", unique = true)
    private String numeroDoc;

    @Column(name = "LUGARORIGEN")
    private String lugarOrigen;

    @Column(name = "PAISORIGEN")
    private String paisOrigen;

    @Column(name = "LUGARDESTINO")
    private String lugarDestino;

    @Column(name = "PAISDESTINO")
    private String paisDestino;
}
