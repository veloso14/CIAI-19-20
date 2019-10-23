package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class ClientService(val pets: PetRepository,
                    val clients: ClientRepository,
                    val appointments: AppointmentRepository) {


    fun appointmentsOfClient(name: String): List<AppointmentDAO> {
        val client = clients.findByIdWithAppointment(name)
                .orElseThrow { NotFoundException("There is no Client with Id $name") }

        return client.appointments // This redirection has pre-fetching
        //return emptyList()
    }

    fun getOneClient(name: String) =
            clients.findById(name)
                    .orElseThrow { NotFoundException("There is no Client with Id $name") }

  /*  fun bookAppointmentOfPet(name:String,pet: PetDAO, appointment: AppointmentDAO) {
        val client: ClientDAO = getOneClient(name)


        appointment.pet = pet
        appointment.client = client
        appointments.save(appointment)

    }*/

    fun newAppointment(apt: AppointmentDAO) =
            // defensive programming
            if (apt.id != 0L)
                throw PreconditionFailedException("Id must be 0 in insertion")
            else
                appointments.save(apt)

    //fun getAllClients(): List<ClientDAO> = clients.findAll().toList()

    fun petsOfClient(name: String): List<PetDAO> {
        val client = clients.findByIdWithAppointment(name)
                .orElseThrow { NotFoundException("There is no Client with Id $name") }

        //return client.pets
        return emptyList()
    }


    fun newPet(pet: PetDAO) =
            // defensive programming
            if (pet.id != 0L)
                throw PreconditionFailedException("Id must be 0 in insertion")
            else
                pets.save(pet)

    fun removePet(name:String, pet: PetDAO){
        val client: ClientDAO = getOneClient(name)

        client.pets = client.pets.minus(pet)
        clients.save(client)

    }

    //??
    fun deleteClient(name: String) {
        getOneClient(name).let { clients.delete(it) }
    }

  /*  fun addNewClient(client: ClientDAO) =
            // defensive programming
            if (client.id != 0L)
                throw PreconditionFailedException("Id must be 0 in insertion")
            else
                clients.save(client) */



}