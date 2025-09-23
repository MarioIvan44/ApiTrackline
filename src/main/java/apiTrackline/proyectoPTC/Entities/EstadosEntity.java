package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TB_ESTADOS")
public class EstadosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estadosSeq")
    @SequenceGenerator(name = "estadosSeq", sequenceName = "SEQ_ID_EST", allocationSize = 1)
    @Column(name = "IDESTADO")
    private Long idEstado;

    //Estados -------- Selectivo
    @ManyToOne
    @JoinColumn(name = "IDSELECTIVO", referencedColumnName = "IDSELECTIVO")
    private SelectivoEntity Selectivo;


    @Column(name = "DOCUMENTOS")
    private Boolean documentos;

    @Column(name = "CLASIFICACION")
    private Boolean clasificacion;

    @Column(name = "DIGITACION")
    private Boolean digitacion;

    @Column(name = "REGISTRO")
    private Boolean registro;

    @Column(name = "PAGO")
    private Boolean pago;

    @Column(name = "LEVANTEPAGO")
    private Boolean levantePago;

    @Column(name = "EQUIPOTRANSPORTE")
    private Boolean equipoTransporte;

    @Column(name = "CARGA")
    private Boolean carga;

    @Column(name = "ENCAMINO")
    private Boolean enCamino;

    @Column(name = "ENTREGADA")
    private Boolean entregada;

    @Column(name = "FACTURACION")
    private Boolean facturacion;
}
