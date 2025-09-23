package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "TB_TIPOCLIENTES")
public class TipoClienteEntity {
    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoClienteSeq")
    @SequenceGenerator(name = "tipoClienteSeq", sequenceName = "SEQ_ID_TC", allocationSize = 1)
    @Column(name = "IDTIPOCLIENTE")
    private Long idTipoCliente;

    @Column(name = "TIPO")
    private String tipo;
}
