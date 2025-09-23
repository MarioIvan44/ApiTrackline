package apiTrackline.proyectoPTC.Config.Security;

import apiTrackline.proyectoPTC.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    // Configuración de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST , "/apiUsuario/postUsuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/apiClientes/agregarCliente").permitAll()
                        .requestMatchers(HttpMethod.POST ,  "/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/logout").authenticated()
                        .requestMatchers("/api/auth/me").authenticated()

                //ENDPOINTS PARA ADUANA
                                .requestMatchers(HttpMethod.GET, "/apiAduana/buscarAduanaPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiAduana/datosAduana").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiAduana/agregarAduana").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiAduana/actualizarAduana/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiAduana/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiAduana/eliminarAduana/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA CARGOS
                                .requestMatchers(HttpMethod.GET, "/apiCargos/obtenerCargoPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiCargos/obtenerDatos").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiCargos/agregarCargo").hasAnyAuthority("ROLE_Administrador" ,  "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiCargos/actualizarCargo/{id}").hasAnyAuthority("ROLE_Administrador" ,  "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiCargos/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador" ,  "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiCargos/eliminarCargo/{id}").hasAnyAuthority("ROLE_Administrador" ,  "ROLE_Empleado")

                //ENDPOINTS PARA CLIENTES
                                .requestMatchers(HttpMethod.GET, "/apiClientes/datosClientes").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiClientes/buscarCliente").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiClientes/clientes").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/apiClientes/actualizarCliente/{nit}").hasAuthority("ROLE_Administrador")
                                .requestMatchers(HttpMethod.PATCH, "/apiClientes/actualizarParcialmente/{nit}").hasAuthority("ROLE_Administrador")
                                .requestMatchers(HttpMethod.DELETE, "/apiClientes/eliminarCliente/{nit}").hasAuthority("ROLE_Administrador")

                //ENDPOINTS PARA EMPLEADOS
                                .requestMatchers(HttpMethod.GET, "/apiEmpleados/obtenerEmpleadoPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiEmpleados/datosEmpleados").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiEmpleados/obtenerEmpleados").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiEmpleados/agregarEmpleado").hasAuthority("ROLE_Administrador")
                                .requestMatchers(HttpMethod.PUT, "/apiEmpleados/actualizarEmpleado/{id}").hasAuthority("ROLE_Administrador")
                                .requestMatchers(HttpMethod.PATCH, "/apiEmpleados/actualizarParcialmente/{id}").hasAuthority("ROLE_Administrador")
                                .requestMatchers(HttpMethod.DELETE, "/apiEmpleados/eliminarEmpleado/{id}").hasAuthority("ROLE_Administrador")

                //ENDPOINTS PARA ESTADOS
                                .requestMatchers(HttpMethod.GET, "/apiEstados/obtenerEstadoPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiEstados/datosEstados").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiEstados/agregarEstado").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiEstados/actualizarEstado/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado", "ROLE_Transportista")
                                .requestMatchers(HttpMethod.PATCH, "/apiEstados/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado", "ROLE_Transportista")
                                .requestMatchers(HttpMethod.DELETE, "/apiEstados/eliminarEstado/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA FINANCIAMIENTO
                                .requestMatchers(HttpMethod.GET, "/apiFinanciamiento/obtenerDatos").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiFinanciamiento/agregarFinanciamiento").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiFinanciamiento/actualizarFinanciamiento/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiFinanciamiento/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiFinanciamiento/eliminarFinanciamiento/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA INFOEMBARQUE
                                .requestMatchers(HttpMethod.GET, "/apiInfoEmbarque/datosInfoEmbarque").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiInfoEmbarque/agregarInfoEmbarque").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiInfoEmbarque/actualizarInfoEmbarque/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiInfoEmbarque/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiInfoEmbarque/eliminarInfoEmbarque/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                // ENDPOINTS PARA OBSERVACIONES
                                .requestMatchers(HttpMethod.GET, "/apiObservaciones/obtenerObservacionPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiObservaciones/datosObservaciones").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiObservaciones/agregarObservacion").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiObservaciones/actualizarObservacion/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiObservaciones/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiObservaciones/eliminarObservacion/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                // ENDPOINTS PARA ORDEN ENCABEZADO
                                .requestMatchers(HttpMethod.GET, "/apiOrden/dataOrden").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiOrden/buscarPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiOrden/postOrden").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiOrden/updateOrden/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiOrden/updateOrdenPartial/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiOrden/deleteOrden/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                // ENDPOINTS PARA ORDEN PERMISOS
                                .requestMatchers(HttpMethod.GET, "/apiOrdenPermisos/obtenerDatos").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiOrdenPermisos/obtenerOrdenPermisoPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiOrdenPermisos/agregarOrdenPermiso").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiOrdenPermisos/actualizarOrdenPermiso/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiOrdenPermisos/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiOrdenPermisos/eliminarOrdenPermiso/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA ORDENSERVICIO

                                .requestMatchers(HttpMethod.GET, "/apiOrdenServicio/buscarPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiOrdenServicio/buscarUltimoId").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiOrdenServicio/datosOrdenesServicio").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiOrdenServicio/validarOrdenCliente/{id}/{nit}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiOrdenServicio/crear").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiOrdenServicio/actualizar/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiOrdenServicio/actualizarParcial/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiOrdenServicio/eliminar/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA PERMISOS

                                .requestMatchers(HttpMethod.GET, "/apiPermisos/obtenerPermisoPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiPermisos/datosPermiso").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiPermisos/agregarPermiso").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiPermisos/actualizarPermiso/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiPermisos/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiPermisos/eliminarPermiso/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                // ENDPOINTS PARA RECOLECCIÓN
                                .requestMatchers(HttpMethod.GET, "/apiRecoleccion/obtenerDatosPaginados").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiRecoleccion/agregarRecoleccion").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiRecoleccion/actualizarRecoleccion/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiRecoleccion/actualizarParcialmenteRecoleccion/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiRecoleccion/eliminarRecoleccion/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA ROLES
                                .requestMatchers(HttpMethod.GET, "/apiRoles/getRoles").hasAnyAuthority("ROLE_Administrador" , "ROLE_Empleados")

                //ENDPOINTS PARA SELECTIVO
                                .requestMatchers(HttpMethod.GET, "/apiSelectivo/obtenerSeletivos").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA SERVICIOTRANSPORTE
                                .requestMatchers(HttpMethod.GET, "/apiServicioTransporte/data").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiServicioTransporte/postST").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiServicioTransporte/updateTS/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiServicioTransporte/patchST/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiServicioTransporte/deleteST/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA TIPOCLIENTE
                                .requestMatchers(HttpMethod.GET, "/apiTipoCliente/obtenerTodos").authenticated()

                //ENDPOINTS PARA TIPODATOCONTABLE
                                .requestMatchers(HttpMethod.GET, "/apiTipoDatoContable/obtenerDatoContablePorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiTipoDatoContable/datosContables").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiTipoDatoContable/agregarTipoDatoContable").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiTipoDatoContable/actualizarDatoContable/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiTipoDatoContable/actualizarParcialmente/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiTipoDatoContable/eliminarDatoContable/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA TIPOFINANCIAMIENTO
                                .requestMatchers(HttpMethod.GET, "/apiTipoF/obtenerTF").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiTipoF/buscarPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiTipoF/crear").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiTipoF/actualizar/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiTipoF/patch/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiTipoF/eliminar/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")

                //ENDPOINTS PARA TIPO SERVICIO
                                .requestMatchers(HttpMethod.GET, "/apiTipoServicio/data").authenticated()

                // ENDPOINTS PARA TRANSPORTE
                                .requestMatchers(HttpMethod.GET, "/apiTransporte/obtenerTransportePorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiTransporte/get").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiTransporte/agregar").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiTransporte/actualizar/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiTransporte/actualizarParcial/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiTransporte/eliminar/{id}").hasAuthority("ROLE_Administrador")

                // ENDPOINTS PARA TRANSPORTISTA
                                .requestMatchers(HttpMethod.GET, "/apiTransportista/dataTransportista").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiTransportista/postTransportista").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiTransportista/updateTransportista/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiTransportista/updateTransportistaPartial/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.DELETE, "/apiTransportista/deleteTransportista/{id}").hasAuthority("ROLE_Administrador")

                // ENDPOINTS PARA USUARIO
                                .requestMatchers(HttpMethod.GET, "/apiUsuario/dataUsuario").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiUsuario/obtenerUsuarioPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiUsuario/buscarUsuarioPorNombre/{usuario}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/apiUsuario/updateUsuario/{id}").hasAuthority("ROLE_Administrador")
                                .requestMatchers(HttpMethod.PATCH, "/apiUsuario/patchUsuario/{id}").hasAuthority("ROLE_Administrador")
                                .requestMatchers(HttpMethod.DELETE, "/apiUsuario/deleteUsuario/{id}").hasAuthority("ROLE_Administrador")

                // ENDPOINTS PARA VIAJE
                                .requestMatchers(HttpMethod.GET, "/apiViaje/buscarPorId/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiViaje/datosViaje").authenticated()
                                .requestMatchers(HttpMethod.GET, "/apiViaje/obtener").authenticated()
                                .requestMatchers(HttpMethod.POST, "/apiViaje/crear").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PUT, "/apiViaje/actualizar/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Empleado")
                                .requestMatchers(HttpMethod.PATCH, "/apiViaje/actualizarParcial/{id}").hasAnyAuthority("ROLE_Administrador", "ROLE_Transportista")
                                .requestMatchers(HttpMethod.DELETE, "/apiViaje/eliminar/{id}").hasAuthority("ROLE_Administrador")

                         .anyRequest().authenticated()
                )
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Exponer el AuthenticationManager como bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
