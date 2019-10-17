package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*


@Service
class AdminService(
        val users: UserRepository,
        val pets: PetRepository,
        val appointments: AppointmentRepository
) {
    // val logger = LoggerFactory.getLogger(AdminService::class.java)

    fun getAllUsers(): List<UserDAO> = users.findAll().toList()

    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();

    // create admin account
    fun hireAdmin(admin: AdminDAO) {
        users.save(admin)
    }

    // create vet account
    fun hireVet(vet: VetDAO) {
        users.save(vet)
    }

    // if employee is admin remove account; if employee is vet freeze account #TODO
    fun fireEmployee(id: Long) {
        val employee = findEmployee(id)
        // if (employee is admin) remove account
        // else if (employee is vet) freeze account
    }

    // returns UserDAO of employee with Id if it exists
    fun findEmployee(id:Long): UserDAO = users.findById(id).orElseThrow {
        NotFoundException("There is no employee with that id: $id")
    }

    // returns the list of appointments of a single Vet by giving his unique Id
    fun getAppointmentsByVetId(id: Long): List<AppointmentDAO> = appointments.findAllByVetId(id).toList()


    // set Vet schedule keeping restrictions in mind
    // #TODO

}


