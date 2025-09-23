package apiTrackline.proyectoPTC.Entities;

import apiTrackline.proyectoPTC.Models.DTO.DTOEstados;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @EqualsAndHashCode @ToString
@Entity
@Table(name = "TB_VIAJES")
public class ViajeEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "viajeSeq")
    @SequenceGenerator(name = "viajeSeq", sequenceName = "SEQ_ID_VIAJE", allocationSize = 1)
    @Column(name = "IDVIAJE")
    private Long idViaje;

    //Relacion con OrdenServicio
    @ManyToOne
    @JoinColumn(name = "IDORDENSERVICIO", referencedColumnName = "IDORDENSERVICIO")
    private OrdenServicioEntity OrdenServicio;

    //Relacion con Transporte
    @ManyToOne
    @JoinColumn(name = "IDTRANSPORTE", referencedColumnName = "IDTRANSPORTE")
    private TransporteEntity transporte;

    @ManyToOne
    @JoinColumn(name = "IDESTADO", referencedColumnName = "IDESTADO")
    private EstadosEntity idEstado;

    //CAMPOS PROPIOS DE VIAJE
    @Column(name = "HORAESTIMADALLEGADA")
    private LocalDateTime horaEstimadaLlegada;

    @Column(name = "HORALLEGADA")
    private LocalDateTime horaLLegada;

    @Column(name = "HORASALIDA")
    private LocalDateTime horaSalida;

    @Column(name = "LUGARPARTIDA")
    private String lugarPartida;

    @Column(name = "COORDENADAPARTIDA")
    private String coordenadaPartida;

    @Column(name = "LUGARLLEGADA")
    private String lugarLLegada;

    @Column(name = "COORDENADALLEGADA")
    private String coordenadaLlegada;

    @Column(name = "PROGRESO")
    private Long progreso;

    @Column(name = "PROGRESOTRANS")
    private String progresoTrans;
}
