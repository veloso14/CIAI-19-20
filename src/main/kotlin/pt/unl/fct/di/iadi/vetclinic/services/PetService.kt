package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetRepository

@Service
class PetService(val pets:PetRepository) {

    fun getAllPets():List<PetDAO> = pets.findAll().toList()

    fun addNewPet(pet:PetDAO) {
        pets.save(pet)
    }

    fun getOnePet(id:Long) =
            pets.findById(id).orElseThrow { PetNotFoundException("There is no Pet with Id $id") }

    fun updatePet(newPet:PetDAO, id:Long) {
        val original:PetDAO = getOnePet(id)

        original.update(newPet)

        pets.save(original)
    }

    fun deletePet(id:Long) {
        val pet:PetDAO = getOnePet(id)

        pets.delete(pet)
    }
}