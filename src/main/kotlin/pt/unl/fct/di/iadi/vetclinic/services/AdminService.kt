package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.util.*
import kotlin.collections.ArrayList


@Service
class AdminService(
        val pets: PetRepository,
        val clients: ClientRepository,
        val appointments: AppointmentRepository,
        val users: UserRepository
) {
    // val logger = LoggerFactory.getLogger(AdminService::class.java)

    fun getAllEmployees(): List<UserDAO> = users.findAll().toList()

    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllClients(): List<UserDAO> = clients.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();

    fun findEmployee(id: String): UserDAO = users.findById(id).orElseThrow { NotFoundException("There is no user with Id $id") }

    // if employee is admin remove account; if employee is vet freeze account
    fun fireEmployee(id:String) {
        val user = findEmployee(id)
        if (user is AdminDAO) {
            users.deleteById(id)
        } else if (user is VetDAO) {
            user.updateFrozen(true)
        }
    }

    // returns the list of appointments of a single Vet by giving his unique Id
    fun getAppointmentsByVetId(id: String): MutableList<AppointmentDAO> {
        val vet = findEmployee(id)
        var appointments = mutableListOf<AppointmentDAO>()
        if (vet is VetDAO) {
            appointments = vet.appointments
        }
        return appointments
    }

    /*

    Em vez de criar um schedule complicado assumimos que todos os vets trabalham
    das 9h as 17h de segunda a sexta. Sendo assim, todas as restricoes dos horarios
    sao verificadas e apenas sera necessario verificar se a data de um appointment
    é valida na marcacao

    fun setVetSchedule(id: String) {
        val vet = findEmployee(id)

        if (vet is VetDAO) {
            // TODO
            //createSchedule(vet)
        } else {
            throw NotFoundException("Vet with given Id:${id} doesn't exist.")
        }
    }

    fun createSchedule(vet: VetDAO) {
        //TODO
    }

 */

}


