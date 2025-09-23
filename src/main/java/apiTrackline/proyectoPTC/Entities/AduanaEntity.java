package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "TB_ADUANA")
public class AduanaEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aduanaSeq")
    @SequenceGenerator(name = "aduanaSeq", sequenceName = "SEQ_ID_ADU", allocationSize = 1)
    @Column(name = "IDADUANA")
    private Long idAduana;

    //LLave foránea hacia la tabla tb_tipoServicio
    @ManyToOne
    @JoinColumn(name = "IDTIPOSERVICIO", referencedColumnName = "IDTIPOSERVICIO")
    private TipoServicioEntity tipoServicio;

    @Column(name = "DM")
    private String DM;

    @Column(name = "PRIMERAMODALIDAD")
    private String primeraModalidad;

    @Column(name = "SEGUNDAMODALIDAD")
    private String segundaModalidad;

    @Column(name = "DIGITADOR")
    private String digitador;

    @Column(name = "TRAMITADOR")
    private String tramitador;
}
