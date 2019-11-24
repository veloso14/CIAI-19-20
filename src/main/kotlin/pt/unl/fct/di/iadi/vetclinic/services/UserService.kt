package pt.unl.fct.di.iadi.vetclinic.services


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.util.*


@Service
class UserService(
                   val users: UserRepository

) {

    fun getUserRole(id: Long): String = getOneUser(id).role

    fun getOneUser(id: Long): UserDAO =
            users.findById(id)
                    .orElseThrow { NotFoundException("There is no user with Id $id") }


    fun updateUser(id: Long, user: UserDAO) =
            getOneUser(id).let { it.update(user); users.save(it) }

    fun updatePassword(id: Long, password: String) = getOneUser(id).let { it.changePassword(BCryptPasswordEncoder().encode(password)); users.save(it) }



}