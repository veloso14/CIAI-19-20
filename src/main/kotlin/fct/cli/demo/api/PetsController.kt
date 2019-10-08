package fct.cli.demo.api

import fct.cli.demo.dto.PetDTO
import fct.cli.demo.model.PetDAO
import fct.cli.demo.services.PetService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pets")
@Controller
@ResponseBody

class PetsController(val pets: PetService){

    @ApiOperation("Retrives info about pet with id id" , response = List::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso")
    ])
    @GetMapping("/")
    fun listAllPets() : List<PetDTO> =
            pets.getAllPets().map{
                PetDTO(it.id , it.name , it.species)
            }

    @PostMapping("")
    fun addNewPet(@RequestBody pet:PetDTO) =
            pets.addOnePet(PetDAO(pet.id , pet.name , pet.species))

    @ApiOperation("Retrives info about pet with id id" , response = PetDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso")
    ])
    @GetMapping("/{id}")
    fun getPetById(@PathVariable id:Long) = PetDTO(id , "nam00", "Dog")

}