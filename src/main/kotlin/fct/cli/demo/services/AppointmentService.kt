package fct.cli.demo.services



import fct.cli.demo.dto.PetDTO
import fct.cli.demo.exceptions.PetNotFoundException
import org.springframework.stereotype.Service

@Service
class AppointmentService{

    var pet: PetDTO? = null

    fun retrievePets():List<PetDTO> = emptyList()

    fun newPet(dto: PetDTO): PetDTO {

        return pet ?: throw PetNotFoundException(-1)

    }


}