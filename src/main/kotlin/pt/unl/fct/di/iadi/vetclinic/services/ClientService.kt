package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class ClientService(val pets: PetRepository,
                    val clients: ClientRepository,
                    val appointments: AppointmentRepository) {


    fun appointmentsOfClient(id: Long): List<AppointmentDAO> {
        val client = clients.findByIdWithAppointment(id)
                .orElseThrow { NotFoundException("There is no Client with Id $id") }

        return client.appointments // This redirection has pre-fetching

    }

    fun getOneClient(id: Long): ClientDAO =
            clients.findById(id)
                    .orElseThrow { NotFoundException("There is no Client with Id $id") }

    fun getAllClients(): List<ClientDAO> = clients.findAll().toList()

    fun newAppointment(apt: AppointmentDAO) =
            // defensive programming
            if (apt.id != 0L)
                throw PreconditionFailedException("Id must be 0 in insertion")
            else
                appointments.save(apt)


    fun petsOfClient(id: Long): List<PetDAO> {
        val client = clients.findByIdWithPet(id)
                .orElseThrow { NotFoundException("There is no Client with Id $id") }

        return client.pets
    }


    fun newPet(pet: PetDAO) =
            // defensive programming
            if (pet.id != 0L)
                throw PreconditionFailedException("Id must be 0 in insertion")
            else
                pets.save(pet)

    fun deletePet(id:Long) = pets.getOne(id).let { it.frozenPet();pets.save(it) }

    fun updateUser(id: Long, user: ClientDAO) =
            getOneClient(id).let { it.update(user); clients.save(it) }

    fun updatePassword(id: Long, password: String) = getOneClient(id).let { it.changePassword(password); clients.save(it) }


    }




