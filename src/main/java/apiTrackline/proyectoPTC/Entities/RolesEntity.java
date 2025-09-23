package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "TB_ROLES")
public class RolesEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolesSeq")
    @SequenceGenerator(name = "rolesSeq", sequenceName = "SEQ_ID_ROL", allocationSize = 1)
    @Column(name = "IDROL")
    private Long idRol;

    @Column(name = "ROL")
    private String rol;

    //Atributo extra para hacer una relación
    @OneToMany(mappedBy = "Rol", cascade = CascadeType.ALL)
    private List<UsuarioEntity> usuarios = new ArrayList<>();
}
