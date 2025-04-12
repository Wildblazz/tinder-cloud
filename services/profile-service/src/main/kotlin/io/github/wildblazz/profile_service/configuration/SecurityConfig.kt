import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    @kotlin.Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorize ->
            authorize.anyRequest().authenticated()
        }.oauth2Login { oauth2 ->
            oauth2.loginPage("/oauth2/authorization/keycloak").defaultSuccessUrl("/", true)
        }.logout { logout ->
            logout.logoutSuccessUrl("/").invalidateHttpSession(true).clearAuthentication(true)
                .deleteCookies("JSESSIONID")
        }

        return http.build()
    }
}
