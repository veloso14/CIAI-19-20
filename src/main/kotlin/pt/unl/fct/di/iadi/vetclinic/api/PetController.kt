package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.PetNotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService

@Api(value = "VetClinic Management System - Pet API",
        description = "Management operations of Pets in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/pets")
class PetController(val pets: PetService) {

    @ApiOperation(value = "View a list of registered pets", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllPets() =
            pets.getAllPets().map { PetDTO(it) }

    @ApiOperation(value = "Add a new pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun addNewPet(@RequestBody pet: PetDTO) = pets.addNewPet(PetDAO(pet))

    @ApiOperation(value = "Get the details of a single pet by id", response = PetDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved pet details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}")
    fun getOnePet(@PathVariable id: Long) =
            try {
                pets.getOnePet(id)
            } catch (e: PetNotFoundException) {
                throw NotFoundException(e.message ?: "Not Found")
            }

    @ApiOperation(value = "Update a pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}")
    fun updatePet(@RequestBody pet: PetDTO, @PathVariable id: Long) =
            try {
                pets.updatePet(PetDAO(pet), id)
            } catch (e: PetNotFoundException) {
                throw NotFoundException(e.message ?: "Not Found")
            }

    @ApiOperation(value = "Delete a pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @DeleteMapping("/{id}")
    fun deletePet(@PathVariable id: Long) =
            try {
                pets.deletePet(id)
            } catch (e: PetNotFoundException) {
                throw NotFoundException(e.message ?: "Not Found")
            }

}