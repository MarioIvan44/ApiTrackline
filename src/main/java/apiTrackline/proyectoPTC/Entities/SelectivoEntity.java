package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TB_SELECTIVO")
@Getter
@Setter
public class SelectivoEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "selectivoSeq")
    @SequenceGenerator(name = "selectivoSeq", sequenceName = "SEQ_ID_SELE", allocationSize = 1)
    @Column(name = "IDSELECTIVO")
    private Long idSelectivo;

    @Column(name = "COLORSELECTIVO")
    private String colorSelectivo;

    @OneToMany(mappedBy = "Selectivo", cascade = CascadeType.ALL)
    private List<EstadosEntity> estados;
}
