package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*


@Service
class AdminService(
        val admins: AdminRepository,
        val vets: VetRepository,
        val pets: PetRepository,
        val clients: ClientRepository,
        val appointments: AppointmentRepository
) {
    // val logger = LoggerFactory.getLogger(AdminService::class.java)

    // creates array list with employees list: admins and vets concatenated
    fun getAllEmployees(): List<UserDAO> {
        val employees = ArrayList<UserDAO>()

        val admins = admins.findAll().toList()
        val vets = vets.findAll().toList()

        employees.addAll(admins)
        employees.addAll(vets)

        return employees
    }

    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllClients(): List<UserDAO> = clients.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();

    // create admin account
    fun hireAdmin(admin: AdminDAO) {
        admins.save(admin)
    }

    // create vet account
    fun hireVet(vet: VetDAO) {
        vets.save(vet)
    }

    // if employee is admin remove account; if employee is vet freeze account
    fun fireEmployee(id: String) {
        if (admins.findById(id).isPresent) {
            admins.deleteById(id)
        } else if (vets.findById(id).isPresent) {
            //freeze vet account #TODO
        }
    }

    // returns the list of appointments of a single Vet by giving his unique Id
    // fun getAppointmentsByVetId(id: Long): List<AppointmentDAO> = appointments.findAllByVetId(id).toList()


    // set Vet schedule keeping restrictions in mind
    // #TODO

}


