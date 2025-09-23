package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TB_PERMISOS")
@Getter
@Setter
public class PermisosEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permisosSeq")
    @SequenceGenerator(name = "permisosSeq", sequenceName = "SEQ_ID_PERMI", allocationSize = 1)
    @Column(name = "IDPERMISO")
    private Long idPermiso;

    @Column(name = "NOMBREPERMISO")
    private String nombrePermiso;
}
