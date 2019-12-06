package pt.unl.fct.di.iadi.vetclinic.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
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
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                //Obter json do swagger daqui
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(UserPasswordAuthenticationFilterToJWT("/login", super.authenticationManagerBean(), users),
                        BasicAuthenticationFilter::class.java)
                .addFilterBefore(UserPasswordSignUpFilterToJWT("/signup", users),
                        BasicAuthenticationFilter::class.java)
                .addFilterBefore(JWTAuthenticationFilter(),
                        BasicAuthenticationFilter::class.java)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
                .withUser("default")
                .password(BCryptPasswordEncoder().encode("1234"))
                .authorities(emptyList())
                .roles("ADMIN")
                .and()
                .passwordEncoder(BCryptPasswordEncoder())
                .and()
                .userDetailsService(customUserDetails)
                .passwordEncoder(BCryptPasswordEncoder())

    }
}


