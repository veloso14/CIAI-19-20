package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.ClientService
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import pt.unl.fct.di.iadi.vetclinic.services.VetService


@Api(value = "VetClinic Management System - Pet API",
        description = "Management operations of Pets in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/pets")
class PetController(val pets: PetService, val vets: VetService, val clients: ClientService) {

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_VET')")
    @ApiOperation(value = "View a list of registered pets", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllPets(): List<PetAptsDTO> =
            pets.getAllPets().map {
                PetAptsDTO(PetDTO(it),
                        it.appointments.map { AppointmentDTO(it) })
            }


    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "Add a new pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun addNewPet(@RequestBody pet: PetDTO): PetDTO =
            PetDTO(pets.addNewPet(PetDAO(pet, emptyList(), clients.getOneClient(pet.ownerID))))

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_VET') or ( hasRole( 'ROLE_CLIENT' ) and @securityService.canEditPet(principal, #id) ) ")
    @ApiOperation(value = "Get the details of a single pet by id", response = PetDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved pet details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}")
    fun getOnePet(@PathVariable id: Long): PetAptsDTO =
            handle4xx { pets.getOnePet(id).let { PetAptsDTO(PetDTO(it), it.appointments.map { AppointmentDTO(it) }) } }

    @PreAuthorize("hasRole('ROLE_CLIENT')  and @securityService.canEditPet(principal, #id) ")
    @ApiOperation(value = "Update a pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}")
    fun updatePet(@RequestBody pet: PetUpdateDTO, @PathVariable id: Long) =
            handle4xx { pets.updatePet(PetDAO(pet), id) }

    @PreAuthorize("hasRole('ROLE_CLIENT')  and @securityService.canEditPet(principal, #id) ")
    @ApiOperation(value = "Delete a pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @DeleteMapping("/{id}")
    fun deletePet(@PathVariable id: Long) =
            handle4xx { pets.deletePet(id) }

    @PreAuthorize("hasRole('ROLE_CLIENT')  and @securityService.canEditPet(principal, #id) ")
    @ApiOperation(value = "List the appointments related to a Pet", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of appointments"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}/appointments")
    fun appointmentsOfPet(@PathVariable id: Long): List<AppointmentDTO> =
            handle4xx {
                pets.appointmentsOfPet(id)
                        .map { AppointmentDTO(it) }
            }



    @PreAuthorize("hasRole('ROLE_CLIENT')  and @securityService.canEditPet(principal, #id) ")
    @ApiOperation(value = "Add a new appointment to a pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added an appointment to a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @PostMapping("/{id}/appointments")
    fun newAppointment(@PathVariable id: Long,
                       @RequestBody apt: AppointmentDTO) =
            handle4xx {
                AppointmentDTO(pets.newAppointment(AppointmentDAO(apt, pets.getOnePet(id), pets.getOnePet(id).owner, vets.getOneVet(apt.vetID))))
            }
}