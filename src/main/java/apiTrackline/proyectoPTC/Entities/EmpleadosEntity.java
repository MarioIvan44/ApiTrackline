package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "TB_EMPLEADOS")
public class EmpleadosEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleadosSeq")
    @SequenceGenerator(name = "empleadosSeq", sequenceName = "SEQ_ID_EMP", allocationSize = 1)
    @Column(name = "IDEMPLEADO")
    private Long idEmpleado;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "TELEFONO")
    private  String telefono;

    @Column(name = "CORREO", unique = true)
    private String correo;

    @Column(name = "NIT", unique = true)
    private String nit;

    @ManyToOne
    @JoinColumn(name = "IDUSUARIO", referencedColumnName = "IDUSUARIO")
    private UsuarioEntity usuarioEmpleado;
}
