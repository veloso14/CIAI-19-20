package pt.unl.fct.di.iadi.vetclinic.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.UserRepository
import pt.unl.fct.di.iadi.vetclinic.services.UserService


class CustomUserDetails(
        private val aUsername: String,
        private val aPassword: String,
        private val someAuthorities: MutableCollection<out GrantedAuthority>) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = someAuthorities

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = aUsername

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = aPassword

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}


@Service
class CustomUserDetailsService(
        val users: UserRepository,
        val userService: UserService
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {

        username?.let {
            val userDAO = users.findByUsername(username)
            if (userDAO.isPresent) {
                return CustomUserDetails(userDAO.get().username, userDAO.get().password, userService.getAuthorities(userDAO.get().username))
            } else
                throw UsernameNotFoundException(username)
        }
        throw UsernameNotFoundException(username)
    }
}