package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DTOViaje {

    //Llave primaria
    private Long idViaje;

    // ------- ORDEN DE SERVICIO RELACIÓN -------
    @NotNull(message = "El ID de orden de servicio es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El ID de orden de servicio debe ser positivo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idOrdenServicio;
    private String clienteNIT;
    private String nombreCliente;
    private String apellidoCliente;
    private String telefonoCliente;
    private String correoCliente;
    private String codEmpresaCliente;
    private Long idUsuarioCliente;
    private String nombreUsuarioCliente;
    //Tb_OrdenEncabezado
    private Long idOrdenEncabezado;
    private LocalDate fechaOrden;
    private String encargadoUno;
    private String referencia;
    private String importador;
    private String nitUno;
    private String registroIvaUno;
    private String facturaA;
    private String encargadoDos;
    private String nitDos;
    private String registroIvaDos;

    //Tb_Aduana
    private Long idAduana;
    private String dm;
    private String primeraModalidad;
    private String segundaModalidad;
    private String digitador;
    private String tramitador;
    private Long idTipoServicio;
    private String nombreTipoServicio;

    //Fin relación orden servicio

    // ------- TRANSPORTE DEL VIAJE  Relación: Transporte -------
    @NotNull(message = "El ID de transporte es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El ID de transporte debe ser positivo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idTransporteViaje;
    //Transporte ------- Transportista
    private Long idTransportistaViaje;
    private String nombreTransportistaViaje;
    private String apellidoTransportistaViaje;
    private String telefonoTransportistaViaje;
    private String correoTransportistaViaje;
    private String nitTransportistaViaje;
    //Transportista ---------- Usuarios
    private Long idUsuarioTransportistaViaje;
    private String nombreUsuarioTransportistaViaje;

    //Transporte ------ ServicioTransporte
    private Long idServicioTransporteViaje;
    private String placaServicioViaje;
    private String tarjetaCirculacionServicioViaje;
    private String capacidadServicioViaje;
    //Fin de relación de Tb_Transporte

    //RELACIÓN CON Tb_Estados
    //Viaje ----------- Estados
    @NotNull(message = "El ID de estados es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El ID de estados debe ser positivo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idEstado;
    private Boolean documentos;
    private Boolean clasificacion;
    private Boolean digitacion;
    private Boolean registro;
    private Boolean pago;
    @Positive(message = "El idSelectivo no puede ser negativo", groups = {DTOEstados.OnCreate.class, DTOEstados.OnPatch.class, DTOEstados.OnUpdate.class})
    private Long idSelectivo;
    private String colorSelectivo;
    private Boolean levantePago;
    private Boolean equipoTransporte;
    private Boolean carga;
    private Boolean enCamino;
    private Boolean entregada;
    private Boolean facturacion;

    //CAMPOS PROPIOS DE VIAJE
    private LocalDateTime horaEstimadaLlegada;
    private LocalDateTime horaLLegada;
    private LocalDateTime horaSalida;
    private String lugarPartida;
    private String coordenadaPartida;
    private String lugarLLegada;
    private String coordenadaLlegada;
    private Long progreso;
    private String progresoTrans;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}
