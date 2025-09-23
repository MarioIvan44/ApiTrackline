package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
@Entity
@Table(name = "TB_TRANSPORTE")
public class TransporteEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transporteSeq")
    @SequenceGenerator(name = "transporteSeq", sequenceName = "SEQ_ID_TRANS", allocationSize = 1)
    @Column(name = "IDTRANSPORTE")
    private Long idTransporte;

    @ManyToOne
    @JoinColumn(name = "IDTRANSPORTISTA", referencedColumnName = "IDTRANSPORTISTA")
    private TransportistaEntity transportista;

    @ManyToOne
    @JoinColumn(name = "IDSERVICIOTRANSPORTE", referencedColumnName = "IDSERVICIOTRANSPORTE")
    private ServicioTransporteEntity servicioTransporte;
}
