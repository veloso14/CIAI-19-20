package pt.unl.fct.di.iadi.vetclinic.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class UserService(
        val users: UserRepository
) {

    val logger = LoggerFactory.getLogger(UserService::class.java)

    //fazer
    fun signOut(id:Long): Boolean = true

    fun getOneUser(id: Long) =
            users.findById(id)
                    .orElseThrow { NotFoundException("There is no user with Id $id") }

    fun updateUser(newUser: UserDAO, id: Long) =
            getOneUser(id).let { it.update(newUser); users.save(it) }

    fun updatePassword(id: Long, password: String) = getOneUser(id).let { it.changePassword(password); users.save(it) }
}