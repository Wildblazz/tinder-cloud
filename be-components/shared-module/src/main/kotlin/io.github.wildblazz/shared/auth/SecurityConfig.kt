package io.github.wildblazz.shared.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/profiles").permitAll() // Allow creating profiles
                    .anyRequest().authenticated()
            }
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/api/profiles")
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
            .exceptionHandling { exceptions ->
                exceptions
                    .accessDeniedHandler(customAccessDeniedHandler)
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
            }
            .logout { logout ->
                logout.logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
            }

        return http.build()
    }

    private fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val authorities = mutableListOf<GrantedAuthority>()

            val realmAccess = jwt.claims["realm_access"] as? Map<*, *>
            val realmRoles = realmAccess?.get("roles") as? List<*>
            realmRoles?.forEach { role ->
                if (role is String) {
                    authorities.add(SimpleGrantedAuthority("ROLE_$role"))
                }
            }

            val resourceAccess = jwt.claims["resource_access"] as? Map<*, *>
            resourceAccess?.forEach { (_, access) ->
                val clientRoles = (access as? Map<*, *>)?.get("roles") as? List<*>
                clientRoles?.forEach { role ->
                    if (role is String) {
                        authorities.add(SimpleGrantedAuthority("ROLE_$role"))
                    }
                }
            }
            authorities
        }
        return converter
    }
}
