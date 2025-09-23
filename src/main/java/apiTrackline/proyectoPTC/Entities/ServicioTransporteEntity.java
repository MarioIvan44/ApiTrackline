package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TB_SERVICIOTRANSPORTE")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ServicioTransporteEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicioTransporteSeq")
    @SequenceGenerator(name = "servicioTransporteSeq", sequenceName = "SEQ_ID_ST", allocationSize = 1)
    @Column(name = "IDSERVICIOTRANSPORTE")
    private Long idServicioTransporte;

    @Column(name = "PLACA", unique = true)
    private String placa;

    @Column(name = "TARJETACIRCULACION", unique = true)
    private String tarjetaCirculacion;

    @Column(name = "CAPACIDAD")
    private String capacidad;
}