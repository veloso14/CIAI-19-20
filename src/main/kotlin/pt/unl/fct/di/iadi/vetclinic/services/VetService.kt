package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.api.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class VetService(val vets: VetRepository,
                    val appointments: AppointmentRepository,
                 val pets: PetRepository,
                 val clients: ClientRepository,
                 val schedules: VetScheduleRepository) {



    fun getOneVet(name: String): VetDAO =
            vets.findById(name)
                    .orElseThrow { NotFoundException("There is no Client with Id $name") }

    fun completeAppointment(apt: AppointmentDAO){
        apt.updateComplete(true)
        appointments.save(apt)
    }

      fun scheduleOfVet(name: String): VetScheduleDAO {

        val schedule = schedules.findByVetId(name).orElseThrow { NotFoundException("There is no Pet with Id $name") }
        return schedule // This redirection has pre-fetching
    }



    fun appointmentsOfVet(name: String): List<AppointmentDAO> {
        val vet = vets.findByIdWithAppointment(name)
                .orElseThrow { NotFoundException("There is no Pet with Id $name") }

        return vet.appointments // This redirection has pre-fetching
    }

    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllClients(): List<ClientDAO> = clients.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();




}