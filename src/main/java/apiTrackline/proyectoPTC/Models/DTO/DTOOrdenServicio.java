package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class DTOOrdenServicio {
    private Long idOrdenServicio;

    // Cliente
    @NotNull(message = "El 'clienteNIT' es obligatorio", groups = {OnCreate.class})
    private String clienteNIT;
    private String nombreCliente;
    private String apellidoCliente;
    private String telefonoCliente;
    private String correoCliente;
    private String codEmpresaCliente;
    //Cliente ------> Usuario
    private Long idUsuarioCliente;
    private String nombreUsuarioCliente;

    // Orden Encabezado
    @Positive(message = "El id de orden encabezado no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
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

    // Info Embarque
    @Positive(message = "El id de info embarque no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idInfoEmbarque;
    private String facturasEmbarque;
    private String proveedorRefEmbarque;
    private Long bultosEmbarque;
    private String tipoEmbarque;
    private Double kilosEmbarque;
    private Double volumenEmbarque;

    // Aduana
    @Positive(message = "El id de aduana no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idAduana;
    private String dm;
    private String primeraModalidad;
    private String segundaModalidad;
    private String digitador;
    private String tramitador;
    //Aduana ----->tipoServicio
    private Long idTipoServicio;
    private String nombreTipoServicio;
    
    // Transporte
    @Positive(message = "El id de transporte no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idTransporte;
    //Transporte -----> Transportista
    private Long idTransportista;
    private String nombreTransportista;
    private String apellidoTransportista;
    private String telefonoTransportista;
    private String correoTransportista;
    private String nitTransportista;
    //Transportista -----> Usuario
    private Long idUsuarioTransportista;
    private String nombreUsuarioTransportista;
    //Transporte -----> ServicioTransporte
    private Long idServicioTransporte;
    private String placaServicio;
    private String tarjetaCirculacionServicio;
    private String capacidadServicio;

    // Recolección
    @Positive(message = "El id de recoleccion no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idRecoleccion;
    private Boolean transporteRecoleccion;
    private Boolean recoleccionEntregaRecoleccion;
    private String numeroDoc;
    private String lugarOrigen;
    private String paisOrigen;
    private String lugarDestino;
    private String paisDestino;

    // Observaciones
    @Positive(message = "El id de observaciones no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idObservaciones;
    //Observaciones -----> Selectivo
    @Positive(message = "El id de selectivo no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idSelectivo;
    private String colorSelectivo;
    //Observaciones ----- campoPropio
    private String textoObservacion;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}