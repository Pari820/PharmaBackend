package pe.edu.upeu.PharmaBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import pe.edu.upeu.PharmaBackend.security.JwtAuthenticationFilter;
import pe.edu.upeu.PharmaBackend.security.JwtService;
import pe.edu.upeu.PharmaBackend.security.RestAccessDeniedHandler;
import pe.edu.upeu.PharmaBackend.security.RestAuthenticationEntryPoint;

import static pe.edu.upeu.PharmaBackend.enums.Permiso.CATALOGO_ESCRIBIR;
import static pe.edu.upeu.PharmaBackend.enums.Permiso.CATALOGO_LEER;
import static pe.edu.upeu.PharmaBackend.enums.Permiso.CLIENTES_ESCRIBIR;
import static pe.edu.upeu.PharmaBackend.enums.Permiso.CLIENTES_LEER;
import static pe.edu.upeu.PharmaBackend.enums.Permiso.USUARIOS_ADMINISTRAR;
import static pe.edu.upeu.PharmaBackend.enums.Permiso.VENTAS_LEER;
import static pe.edu.upeu.PharmaBackend.enums.Permiso.VENTAS_REGISTRAR;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // =========================================
    // RUTAS PUBLICAS
    // =========================================

    private static final String[] RUTAS_PUBLICAS = {
            "/api/v1/auth/login",
            "/api/health",

            // PRACTICA 13 - ACTUATOR HEALTH PUBLICO
            "/actuator/health",
            "/actuator/health/**",

            // SWAGGER
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/error"
    };

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            CorsConfigurationSource corsConfigurationSource,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler
    ) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(
                        jwtService,
                        userDetailsService
                );

        http
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // =========================================
                // CORS
                // =========================================

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource
                        )
                )

                // =========================================
                // SESIONES SIN ESTADO - JWT
                // =========================================

                .sessionManagement(sesion ->
                        sesion.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================================
                        // RUTAS PUBLICAS
                        // =========================================

                        .requestMatchers(RUTAS_PUBLICAS)
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        )
                        .permitAll()

                        // =========================================
                        // PRACTICA 13 - ACTUATOR PROTEGIDO
                        // =========================================

                        // Health ya es publico.
                        // Los demas endpoints requieren
                        // permiso USUARIOS_ADMINISTRAR.

                        .requestMatchers("/actuator/**")
                        .hasAuthority(
                                USUARIOS_ADMINISTRAR.name()
                        )

                        // =========================================
                        // CATALOGO - LECTURA
                        // =========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/categorias/**",
                                "/api/v1/productos/**"
                        )
                        .hasAuthority(
                                CATALOGO_LEER.name()
                        )

                        // =========================================
                        // CATALOGO - ESCRITURA
                        // =========================================

                        .requestMatchers(
                                "/api/v1/categorias/**",
                                "/api/v1/productos/**"
                        )
                        .hasAuthority(
                                CATALOGO_ESCRIBIR.name()
                        )

                        // =========================================
                        // CLIENTES - LECTURA
                        // =========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/clientes/**"
                        )
                        .hasAuthority(
                                CLIENTES_LEER.name()
                        )

                        // =========================================
                        // CLIENTES - ESCRITURA
                        // =========================================

                        .requestMatchers(
                                "/api/v1/clientes/**"
                        )
                        .hasAuthority(
                                CLIENTES_ESCRIBIR.name()
                        )

                        // =========================================
                        // VENTAS - REGISTRAR
                        // =========================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/ventas"
                        )
                        .hasAuthority(
                                VENTAS_REGISTRAR.name()
                        )

                        // =========================================
                        // VENTAS - LECTURA
                        // =========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/ventas/**"
                        )
                        .hasAuthority(
                                VENTAS_LEER.name()
                        )

                        // =========================================
                        // VENTAS - OTRAS OPERACIONES
                        // =========================================

                        .requestMatchers(
                                "/api/v1/ventas/**"
                        )
                        .hasAuthority(
                                VENTAS_REGISTRAR.name()
                        )

                        // =========================================
                        // ADMINISTRACION DE SEGURIDAD
                        // =========================================

                        .requestMatchers(
                                "/api/v1/usuarios/**",
                                "/api/v1/empleados/**",
                                "/api/v1/roles/**"
                        )
                        .hasAuthority(
                                USUARIOS_ADMINISTRAR.name()
                        )

                        // =========================================
                        // RESTO DE ENDPOINTS
                        // =========================================

                        .anyRequest()
                        .authenticated()
                )

                // =========================================
                // MANEJO DE ERRORES DE SEGURIDAD
                // =========================================

                .exceptionHandling(errores ->
                        errores
                                .authenticationEntryPoint(
                                        authenticationEntryPoint
                                )
                                .accessDeniedHandler(
                                        accessDeniedHandler
                                )
                )

                // =========================================
                // FILTRO JWT
                // =========================================

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // =========================================
    // CIFRADO DE CONTRASEÑAS
    // =========================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================================
    // AUTHENTICATION MANAGER
    // =========================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }
}
