package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class VetService(val vets: VetRepository,
                 val appointments: AppointmentRepository,
                 val pets: PetRepository,
                 val clients: ClientRepository,
                 val users: UserRepository) {

    fun getAllVets(): List<VetDAO> = vets.findAll().toList()

    fun getOneVet(id: Long): VetDAO =
            vets.findById(id)
                    .orElseThrow { NotFoundException("There is no vet with Id $id") }

    fun completeAppointment(id:Long, desc:String){

        val apt = appointments.findById(id)
                .orElseThrow { NotFoundException("There is no Appointment with Id $id") }

        apt.complete(desc)
        appointments.save(apt)
    }



    /*
    fun scheduleOfVet(name: String): VetScheduleDAO {

        val schedule = schedules.findByVetId(name).orElseThrow { NotFoundException("There is no Pet with Id $name") }
        return schedule // This redirection has pre-fetching
    }
*/


    fun appointmentsOfVet(id: Long): List<AppointmentDAO> {
        val vet = vets.findByIdWithAppointment(id)
                .orElseThrow { NotFoundException("There is no Pet with Id $id") }

        return vet.appointments // This redirection has pre-fetching
    }
/*
    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllClients(): List<ClientDAO> = clients.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();

 */

    fun hireVet(vet:VetDAO) =
            when {
                vet.id != 0L -> throw PreconditionFailedException("Id must be 0 in insertion")
                users.findByUsername(vet.username).isPresent -> throw PreconditionFailedException("There is already an user with the specified username")
                else -> vets.save(vet)
            }


    fun updateUser(id: Long, user: VetDAO) =
            getOneVet(id).let { it.update(user); vets.save(it) }

    fun updatePassword(id: Long, password: String) = getOneVet(id).let { it.changePassword(password); vets.save(it) }


    fun fireVet(id: Long) = getOneVet(id).let { it.updateFrozen(true); vets.save(it) }
    /* fun fireVet(id:Long){
        val user = getOneVet(id)
        if (user is VetDAO){
            user.updateFrozen(true)
            vets.save(user)
        }
    }

     */



}