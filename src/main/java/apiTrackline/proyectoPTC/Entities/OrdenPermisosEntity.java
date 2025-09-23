package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ORDENPERMISOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrdenPermisosEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordenPermisosSeq")
    @SequenceGenerator(name = "ordenPermisosSeq", sequenceName = "SEQ_ID_OP", allocationSize = 1)
    @Column(name = "IDORDENPERMISOS")
    private Long idOrdenPermisos;

    @ManyToOne
    @JoinColumn(name = "IDPERMISO", referencedColumnName = "IDPERMISO")
    private PermisosEntity idPermiso;
    
    @Column(name = "MARCADO")
    private Boolean marcado;

    @ManyToOne
    @JoinColumn(name = "IDORDENSERVICIO", referencedColumnName = "IDORDENSERVICIO")
    private OrdenServicioEntity ordenServicioPermisos;
}
