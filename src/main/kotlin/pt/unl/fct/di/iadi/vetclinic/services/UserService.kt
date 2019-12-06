package pt.unl.fct.di.iadi.vetclinic.services


import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserRepository
import java.util.*


@Service
class UserService(
                   val users: UserRepository

) {

    fun getUserRoles(name: String): List<String> {
        val u : UserDAO = getOneUserByName(name)
        if(u.role.isNotEmpty()){
           return  u.role.split(",")
        }else return emptyList()

    }


    fun getAuthorities(name: String) : MutableList<GrantedAuthority> {
        var authorities : MutableList<GrantedAuthority>  = mutableListOf()

        getUserRoles(name).forEach { p ->
            val authority = SimpleGrantedAuthority(p)
            authorities.add(authority)
        }

        return authorities

    }


    fun getOneUserByName(name: String): UserDAO =
            users.findByUsername(name)
                    .orElseThrow { NotFoundException("There is no user with username $name") }


    fun getOneUser(id: Long): UserDAO =
            users.findById(id)
                    .orElseThrow { NotFoundException("There is no user with Id $id") }


    fun updateUser(id: Long, user: UserDAO) =
            getOneUser(id).let { it.update(user); users.save(it) }

    fun updatePassword(id: Long, password: String) = getOneUser(id).let { it.changePassword(BCryptPasswordEncoder().encode(password)); users.save(it) }


    fun addUser(user: UserDAO) : Optional<UserDAO> {
        val aUser = users.findByUsername(user.username)

        return if ( aUser.isPresent )
            Optional.empty()
        else {
            user.password = BCryptPasswordEncoder().encode(user.password)
            Optional.of(users.save(user))
        }
    }


}