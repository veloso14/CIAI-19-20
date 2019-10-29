package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class ClientService(val pets: PetRepository,
                    val clients: ClientRepository,
                    val appointments: AppointmentRepository,
                    val petService: PetService) {


    fun appointmentsOfClient(id: Long): List<AppointmentDAO> {
        val client = clients.findByIdWithAppointment(id)
                .orElseThrow { NotFoundException("There is no Client with Id $id") }

        return client.appointments // This redirection has pre-fetching

    }

    fun getOneClient(id: Long): ClientDAO =
            clients.findById(id)
                    .orElseThrow { NotFoundException("There is no Client with Id $id") }



   /* fun newAppointment(apt: AppointmentDAO) =
            // defensive programming
            if (apt.id != 0L)
                throw PreconditionFailedException("Id must be 0 in insertion")
            else
                appointments.save(apt)*/

    fun newAppointment(apt: AppointmentDAO) =
            // defensive programming

                petService.newAppointment(apt)






}