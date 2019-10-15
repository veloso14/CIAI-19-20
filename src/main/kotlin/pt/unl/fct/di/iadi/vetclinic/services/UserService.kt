package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.AppointRepository
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserRepository

@Service
class UserService(val user: UserRepository) {

    //fun getAllAppointment():List<AppointmentDAO> = appointment.findAll().toList()

    fun addNewUser(client: UserDAO) {
        user.save(client)
    }

    fun getOneUser(id:Long) =
            user.findById(id).orElseThrow { PetNotFoundException("There is no client with Id $id") }


    fun deleteUser(id:Long) {
        val client: UserDAO = getOneUser(id)

        user.delete(client)
    }
}