package apiTrackline.proyectoPTC.Config.AppConfig;

import apiTrackline.proyectoPTC.Utils.JWTUtils;
import apiTrackline.proyectoPTC.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public JwtCookieAuthFilter  jwtCookieAuthFilter(JWTUtils jwtUtils){
        return new JwtCookieAuthFilter(jwtUtils);
    }
}

