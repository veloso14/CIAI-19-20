package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class VetService(val vets: VetRepository,
                 val appointments: AppointmentRepository,
                 val pets: PetRepository,
                 val clients: ClientRepository) {



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

    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllClients(): List<ClientDAO> = clients.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();


    fun updateUser(id: Long, user: VetDAO) =
            getOneVet(id).let { it.update(user); vets.save(it) }

    fun updatePassword(id: Long, password: String) = getOneVet(id).let { it.changePassword(password); vets.save(it) }



}