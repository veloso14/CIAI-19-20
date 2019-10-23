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

    fun addNewPet(pet: PetDAO) {
        pets.save(pet)
    }

    fun getOnePet(id: Long) =
            pets.findById(id)
                    .orElseThrow { NotFoundException("There is no Pet with Id $id") }

    fun updatePet(newPet: PetDAO, id: Long) =
            getOnePet(id).let { it.update(newPet); pets.save(it) }

    fun deletePet(id: Long) {
        getOnePet(id).let { pets.delete(it) }
    }

    fun appointmentsOfPet(id: Long): List<AppointmentDAO> {
        val pet = pets.findByIdWithAppointment(id)
                .orElseThrow { NotFoundException("There is no Pet with Id $id") }

        return pet.appointments // This redirection has pre-fetching
    }

    fun newAppointmentOfPet(id: Long, appointment: AppointmentDAO) {
        val pet: PetDAO = getOnePet(id)

        appointment.pet = pet
        appointments.save(appointment)

//        pet.appointments = pet.appointments.plus(appointment)
//        pets.save(pet)
    }

    fun notesOfPet(id: Long): List<String> {
        val pet: PetDAO = getOnePet(id)


        return pet.notes // This redirection has pre-fetching
    }

    fun newNoteOfPet(id: Long, note: String) {
        val pet: PetDAO = getOnePet(id)

        pet.notes = pet.notes.plus(note)
        pets.save(pet)


//        pet.appointments = pet.appointments.plus(appointment)
//        pets.save(pet)
    }


}