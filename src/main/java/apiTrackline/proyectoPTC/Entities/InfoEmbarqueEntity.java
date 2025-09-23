package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TB_INFOEMBARQUE")
@Getter @Setter @ToString @EqualsAndHashCode
public class InfoEmbarqueEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "infoEmbarqueSeq")
    @SequenceGenerator(name = "infoEmbarqueSeq", sequenceName = "SEQ_ID_IE", allocationSize = 1)
    @Column(name = "IDINFOEMBARQUE")
    private Long idInfoEmbarque;

    @Column(name = "FACTURAS")
    private String facturas;

    @Column(name = "PROVEEDORREF")
    private String proveedorRef;

    @Column(name = "BULTOS")
    private Long bultos;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "KILOS")
    private Double kilos;

    @Column(name = "VOLUMEN")
    private Double volumen;
}
