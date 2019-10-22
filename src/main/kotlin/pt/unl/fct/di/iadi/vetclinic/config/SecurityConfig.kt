package pt.unl.fct.di.iadi.vetclinic.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import pt.unl.fct.di.iadi.vetclinic.services.UserService


@Configuration
class SecurityConfig(
        val customUserDetails: CustomUserDetailsService,
        val users: UserService
) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable() // for now, we can disable cross site request forgery protection
                .authorizeRequests()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
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
                .withUser("user")
                .password(BCryptPasswordEncoder().encode("password"))
                .authorities(emptyList())
                .and()
                //Cliente
                .withUser("client")
                .password(BCryptPasswordEncoder().encode("password"))
                .authorities(emptyList())
                .roles("USER","CLIENT")
                .and()
                //Veterinario
                .withUser("vet")
                .password(BCryptPasswordEncoder().encode("password"))
                .authorities(emptyList())
                .roles("USER","VETERINARIO")
                .and()
                //Admin
                .withUser("admin")
                .password(BCryptPasswordEncoder().encode("password"))
                .authorities(emptyList())
                .roles("USER","ADMIN","VETERINARIO")
                .and()
                .passwordEncoder(BCryptPasswordEncoder())
                .and()
                .userDetailsService(customUserDetails)
                .passwordEncoder(BCryptPasswordEncoder())

    }
}


