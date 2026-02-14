package org.alpha.omega.student_microservice.infrastructure.adapter.in.security.config;

import org.alpha.omega.student_microservice.infrastructure.adapter.in.security.contant.SecurityConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/**").permitAll()) // This is temporal
                .build();
    }

    /**
     * Type: Función de hash de contraseñas moderna y configurable.
     * Security:
     *    - Ganador de la Password Hashing Competition (2015).
     *    - Configurable en tiempo, memoria y paralelismo, lo que lo hace mucho más resistente a GPUs y ASICs.
     *    - Soporta protección contra ataques de canal lateral (Argon2i/Argon2id).
     * Use: Recomendado en nuevas aplicaciones donde se quiere máxima seguridad.
     * Practical consideration: Menos integrado “out-of-the-box” que BCrypt en algunos frameworks, pero cada vez más
     * librerías lo soportan (incluyendo Java con argon2-jvm).
     * This is a professional configuration of Argon2id, Spring Security 6 use Argon2id by default.
     * This is blocking, therefore, in webflux, it's should run in: Schedulers.boundedElastic()
     * If hashLength is of -> approx length of encode password:
     *   - 16 bytes  ->  75–85 characters
     *   - 32 bytes  ->  95–105 characters
     *   - 64 bytes  ->  120–140 characters
     * @return PasswordEncoder
     */
    @Bean
    @Primary
    public PasswordEncoder argon2PasswordEncoder() {
        return new Argon2PasswordEncoder(SecurityConstant.Argon2.SALT_LENGTH, SecurityConstant.Argon2.HASH_LENGTH,
                SecurityConstant.Argon2.PARALLELISM, SecurityConstant.Argon2.MEMORY, SecurityConstant.Argon2.ITERATIONS);
    }

    /**
     * Type: Función de hash de contraseñas basada en Blowfish.
     * Security: Probada, resistente a ataques de fuerza bruta, introduce “sal” automáticamente.
     * Limitations:
     *    - Está diseñada para CPU, no permite ajustar memoria usada, solo el costo (factor de trabajo).
     *    - Menos resistente a ataques modernos con hardware especializado (GPUs o ASICs).
     * Use: Muy popular en aplicaciones Java y frameworks como Spring, fácil de usar con BCryptPasswordEncoder.
     * BCryptPasswordEncoder is an: One-Way Hashing Function
     * This is blocking, therefore, in webflux, it's should run in: Schedulers.boundedElastic()
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(SecurityConstant.BCrypt.STRENGTH);
    }
}
