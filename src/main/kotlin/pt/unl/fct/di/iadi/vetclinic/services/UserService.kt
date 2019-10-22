package pt.unl.fct.di.iadi.vetclinic.services

import pt.unl.fct.di.iadi.vetclinic.model.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import java.util.*

@Service
class UserService(val users: UserRepository) {

    fun getAllUser():List<UserDAO> = users.findAll().toList()

  fun addNewUser(client: UserDAO) {
       users.save(client)
   }

   fun getOneUser(id:String) =
            users.findById(id).orElseThrow { NotFoundException("There is no client with Id $id") }


    fun findUser(username:String): Optional<UserDAO> = users.findById(username)

    fun addUser(user: UserDAO) : Optional<UserDAO> {
        val aUser = users.findById(user.username)

        return if ( aUser.isPresent )
            Optional.empty()
        else {
            user.password = BCryptPasswordEncoder().encode(user.password)
            Optional.of(users.save(user))
        }
    }

    fun checkIfPasswordValid(id:String , password : String): Boolean {
        val client: UserDAO = getOneUser(id)
        return client.password.equals(password)
    }

    fun checkIfUserExists(id:String): Boolean {
        return users.existsById(id);
    }

      fun deleteUser(id:String) {
            val client: UserDAO = getOneUser(id)
            users.delete(client)
       }
}