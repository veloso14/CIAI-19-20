package pt.unl.fct.di.iadi.vetclinic.api


import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.VetService


@Api(value = "VetClinic Management System - Pet API",
        description = "Management operations of Pets in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/user/vet")
class VetController(val vets: VetService) {



    @ApiOperation(value = "Get the details of a single vet by id", response = PetDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved vet details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{name}")
    fun getOneVet(@PathVariable name:String) : VetAptsDTO =
            handle4xx { vets.getOneVet(name).let { VetAptsDTO(VetDTO(it), it.appointments.map { AppointmentDTO(it) }) } }


    @ApiOperation(value = "List the appointments related to a Vet", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of appointments"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{name}/appointments")
    fun appointmentsOfVet(@PathVariable name:String): List<AppointmentDTO> =
            handle4xx {
                vets.appointmentsOfVet(name)
                        .map { AppointmentDTO(it) }
            }

    @ApiOperation(value = "View a list of registered pets", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/pets")
    fun getAllPets() = vets.getAllPets().map { PetDTO(it) }

    @ApiOperation(value = "View a list of registered clients", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/clients")
    fun getAllClients() = vets.getAllClients().map { ClientDTO(it) }

    @ApiOperation(value = "View a list of registered clients", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/appointments")
    fun getAllAppointments() = vets.getAllAppointments().map { AppointmentDTO(it) }



}