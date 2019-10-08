package fct.cli.demo.exceptions

import fct.cli.demo.model.PetDAO
import javassist.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND)
class PetNotFoundException(msg : String) : RuntimeException(msg) {

    @Service
    class PetServices{
        fun getAllPets(): List<PetDAO> = emptyList()

        fun getOnePet(id:Long) =
                if (id == 1L)
                    PetDAO(id , "Pantufas" , "Dog")
                else
                    throw NotFoundException("Pet $id not found")
    }
}
