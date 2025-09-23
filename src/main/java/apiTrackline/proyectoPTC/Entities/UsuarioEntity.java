package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TB_USUARIOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class UsuarioEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioSeq")
    @SequenceGenerator(name = "usuarioSeq", sequenceName = "SEQ_ID_US", allocationSize = 1)
    @Column(name = "IDUSUARIO")
    private Long idUsuario;

    @Column(name = "USUARIO", unique = true)
    private String usuario;

    @Column(name = "CONTRASENIA")
    private String contrasenia;

    @ManyToOne
    @JoinColumn(name = "IDROL", referencedColumnName = "IDROL")
    private RolesEntity Rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<ClientesEntity> clientes;

    @OneToMany(mappedBy = "usuarioEmpleado", cascade = CascadeType.ALL)
    private List<EmpleadosEntity> empleados;

    @OneToMany(mappedBy = "usuarioT", cascade = CascadeType.ALL)
    private List<TransportistaEntity> transportistas;
}
