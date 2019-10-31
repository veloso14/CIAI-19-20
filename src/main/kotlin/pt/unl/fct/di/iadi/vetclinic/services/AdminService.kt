package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class AdminService(val admins: AdminRepository,
                 val appointments: AppointmentRepository,
                 val pets: PetRepository,
                 val clients: ClientRepository,
                 val vets: VetRepository,
                 val users: UserRepository,
                 val vetService: VetService  ) {



    fun getOneAdmin(id: Long): AdminDAO =
            admins.findById(id)
                    .orElseThrow { NotFoundException("There is no vet with Id $id") }


    fun getAllAdmins(): List<AdminDAO> = admins.findAll().toList()

    fun getAllVets(): List<VetDAO> = vets.findAll().toList()

    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllClients(): List<ClientDAO> = clients.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();

    fun hireVet(vet:VetDAO) =
        if (vet.id != 0L)
            throw PreconditionFailedException("Id must be 0 in insertion")
        else vets.save(vet)

    fun hireAdmin(admin:AdminDAO) =
        if (admin.id != 0L)
            throw PreconditionFailedException("Id must be 0 in insertion")
        else admins.save(admin)

    fun findEmployee(id: Long): UserDAO = users.findById(id).orElseThrow { NotFoundException("There is no user with Id $id") }

    fun fireVet(id:Long){
        val user = findEmployee(id)
        if (user is VetDAO)
        user.updateFrozen(true)
    }

    fun fireAdmin(id:Long){
        val user = findEmployee(id)
        if (user is AdminDAO)
            users.delete(user)
    }

    fun getVetsAppointments(id:Long): List<AppointmentDAO>{
        return vetService.appointmentsOfVet(id)

    }


    fun updateUser(id: Long, user: AdminDAO) =
            getOneAdmin(id).let { it.update(user); admins.save(it) }

    fun updatePassword(id: Long, password: String) = getOneAdmin(id).let { it.changePassword(password); admins.save(it) }


}