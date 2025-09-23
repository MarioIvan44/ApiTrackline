package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "TB_CLIENTES")
@Entity
public class ClientesEntity {

    @Id
    @Column(name = "CLIENTENIT")
    private String clienteNit;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "CORREO", unique = true)
    private String correo;

    @Column(name = "CODEMPRESA")
    private String codEmpresa;

    @ManyToOne
    @JoinColumn(name = "IDUSUARIO", referencedColumnName = "IDUSUARIO")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "IDTIPOCLIENTE", referencedColumnName = "IDTIPOCLIENTE")
    private TipoClienteEntity tipoCliente;
}
