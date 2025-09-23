package apiTrackline.proyectoPTC.Entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TB_TIPOFINANCIAMIENTOS")
@Getter
@Setter
@ToString
public class TipoFinanciamientosEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoFinanciamientoSeq")
    @SequenceGenerator(name = "tipoFinanciamientoSeq", sequenceName = "SEQ_ID_TF", allocationSize = 1)
    @Column(name = "IDTIPOFINANCIAMIENTO")
    private Long idTipoFinanciamiento;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "PRECIOUNI")
    private Double precioUni;
}

