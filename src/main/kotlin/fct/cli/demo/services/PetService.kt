package fct.cli.demo.services

import fct.cli.demo.dto.PetDTO
import fct.cli.demo.exceptions.PetNotFoundException
import fct.cli.demo.model.PetDAO
import org.springframework.stereotype.Service

@Service
class PetService{

    fun retrievePets():List<PetDTO> = emptyList()

    fun addOnePet(petDTO: PetDAO) {}


    fun getAllPets():List<PetDAO> = emptyList()


    fun getOnepet(id: Long) =
            if(id == 1L)
                PetDAO(id , "Pantufas" , "Dog")
            else
                throw  PetNotFoundException("Pet $id not found")


}