package org.todo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/todo/tasks")).hasRole("USER")
                        .requestMatchers(new AntPathRequestMatcher("/todo/auth")).hasAuthority("ROLE_ADMIN")
                .anyRequest().denyAll()
        ).formLogin(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails details = User.builder()
//                .username("Elango")
//                .password("{noop}Elango")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{noop}admin")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(details,admin);
//    }
}
