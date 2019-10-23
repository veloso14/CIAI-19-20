package pt.unl.fct.di.iadi.vetclinic.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentRepository
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetRepository

@Service
class PetService(
        val pets: PetRepository,
        val appointments: AppointmentRepository
) {

    val logger = LoggerFactory.getLogger(PetService::class.java)

    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun addNewPet(pet: PetDAO) =
        // defensive programming
        if (pet.id != 0L)
            throw PreconditionFailedException("Id must be 0 in insertion")
        else
            pets.save(pet)

    fun getOnePet(id: Long) =
            pets.findById(id)
                .orElseThrow { NotFoundException("There is no Pet with Id $id") }

    fun updatePet(newPet: PetDAO, id: Long) =
        getOnePet(id).let { it.update(newPet); pets.save(it) }

    fun deletePet(id: Long) =
        getOnePet(id).let { pets.delete(it) }

    fun appointmentsOfPet(id: Long): List<AppointmentDAO> {
        val pet = pets.findByIdWithAppointment(id)
                      .orElseThrow { NotFoundException("There is no Pet with Id $id") }

        return pet.appointments // This redirection has pre-fetching
    }

    fun newAppointment(apt: AppointmentDAO) =
        // defensive programming
        if (apt.id != 0L)
            throw PreconditionFailedException("Id must be 0 in insertion")
        else
            appointments.save(apt)
}