package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserRepository

@Service
class UserService(val user: UserRepository) {

    fun getAllUser():List<UserDAO> = user.findAll().toList()

  fun addNewUser(client: UserDAO) {
       user.save(client)
   }

   fun getOneUser(id:Long) =
            user.findById(id).orElseThrow { NotFoundException("There is no client with Id $id") }


    fun checkIfPasswordValid(id:Long , password : String): Boolean {
        val client: UserDAO = getOneUser(id)
        if ( client.password.equals(password)){
            return true
        }
        else{
            return false
        }

    }

    fun checkIfUserExists(id:Long): Boolean {
        return user.existsById(id);
    }

      fun deleteUser(id:Long) {
            val client: UserDAO = getOneUser(id)
            user.delete(client)
       }
}