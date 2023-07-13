package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //configuracion 1
    /*
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/v1/index2").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                // sirve para eniar en el header nuestras credenciales, poco seguro
                .httpBasic()
                .and()
                .build();
    }
*/

    //configuracion 2, la mejor manera basica
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //autorizacion de las url
                .authorizeHttpRequests(auth -> {
                    //aquí puede entrar cualquiera
                    auth.requestMatchers("/v1/index2").permitAll();
                    //Y aquí les prohíbo entrar a las demás
                    auth.anyRequest().authenticated();
                })
                //configuracion del login general
                //como prohibi en la línea de codigo 38 de poder entrar a las demás url,
                //pues le doy de nuevo el permiso para todos registrarse
                .formLogin(form -> {
                    //aquí le doy el permiso
                    form.permitAll();
                    //aqu lo rediriciono a donde quiero que vaya cuando se registre
                    form.successHandler(successHandler());
                })
                .sessionManagement(sesionManager -> {
                    sesionManager.sessionCreationPolicy(SessionCreationPolicy.ALWAYS); //ALWAYS - IF_REQUIRED - NEVER - STATELEES
                    //si la sesion es invalida a donde lo va dirigir
                    sesionManager.invalidSessionUrl("/login");
                    //maximas sesiones por cada usuario
                    sesionManager.maximumSessions(1);
                    sesionManager.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession); //migratesession - newSession - none
                    //si la sesion expira a donde lo va dirigir
                    sesionManager.sessionAuthenticationErrorUrl("/login");
                })
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler() {
        return (((request, response, authentication) -> response.sendRedirect("/v1/session")));
    }

}
