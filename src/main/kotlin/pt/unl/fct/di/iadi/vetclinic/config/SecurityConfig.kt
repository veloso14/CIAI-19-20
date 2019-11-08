package pt.unl.fct.di.iadi.vetclinic.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import pt.unl.fct.di.iadi.vetclinic.services.SecurityService



@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
        val customUserDetails: CustomUserDetailsService,
        val users: SecurityService
) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable() // for now, we can disable cross site request forgery protection
                .authorizeRequests()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/users/all").hasRole("ADMIN")
                .antMatchers("/users/*").hasRole("ADMIN")
                .antMatchers("/appointments").hasRole("ADMIN")
                //Obter json do swagger daqui
                .antMatchers("/v2/api-docs").permitAll()

                //Permite mais endpoints
                .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/users/register/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(UserPasswordAuthenticationFilterToJWT("/users/login", super.authenticationManagerBean()),
                        BasicAuthenticationFilter::class.java)
                .addFilterBefore(UserPasswordSignUpFilterToJWT("/users/register/**", users),
                        BasicAuthenticationFilter::class.java)
                .addFilterBefore(JWTAuthenticationFilter(),
                        BasicAuthenticationFilter::class.java)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
                .withUser("USER")
                .password(BCryptPasswordEncoder().encode("password"))
                .authorities("USER")
                .roles("USER")
                .and()
                //Cliente
                .withUser("CLIENT")
                .password(BCryptPasswordEncoder().encode("password"))
                .authorities(emptyList())
                .roles("CLIENT")
                .and()
                //Veterinario
                .withUser("VET")
                .password(BCryptPasswordEncoder().encode("password"))
                .authorities(emptyList())
                .roles("VET")
                .and()
                //Admin
                .withUser("ADMIN")
                .password(BCryptPasswordEncoder().encode("password"))
                .authorities(emptyList())
                .roles("ADMIN")
                .and()
                .passwordEncoder(BCryptPasswordEncoder())
                .and()
                .userDetailsService(customUserDetails)
                .passwordEncoder(BCryptPasswordEncoder())

    }
}


