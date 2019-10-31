package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ScheduleDAO
import pt.unl.fct.di.iadi.vetclinic.model.VetDAO
import pt.unl.fct.di.iadi.vetclinic.services.VetService


@Api(value = "VetClinic Management System - Vet API",
        description = "Management operations of Vets in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/vets")
class VetController(val vets: VetService) {



    @ApiOperation(value = "Get the details of a single vet by id", response = VetDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved vet details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}")
    fun getOneVet(@PathVariable id:Long) : VetDTO =
            handle4xx { vets.getOneVet(id).let { VetDTO(it) } }




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

    @ApiOperation(value = "View a list of registered appointments", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/appointments")
    fun getAllAppointments() = vets.getAllAppointments().map { AppointmentDTO(it) }

    @ApiOperation(value = "Complete an appointment", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully completed"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/appointments/{id}")
    fun completeAppointment(@RequestBody desc:String, @PathVariable id: Long) =
            handle4xx { vets.completeAppointment(id, desc)}

    @ApiOperation(value = "List the appointments related to a Vet", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of appointments"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}/appointments")
    fun appointmentsOfVet(@PathVariable id: Long): List<AppointmentDTO> =
            handle4xx { vets.appointmentsOfVet(id).map { AppointmentDTO(it) } }


    @ApiOperation(value = "Update contact info of a vet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated a user"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/info/{id}")
    fun updatePet(@RequestBody user: VetDTO, @PathVariable id: Long) =
            handle4xx { vets.updateUser(id, VetDAO(user, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())) }

     @ApiOperation(value = "Change the password of a vet", response = Unit::class)
     @ApiResponses(value = [
         ApiResponse(code = 200, message = "Successfully changed the password"),
         ApiResponse(code = 401, message = "You are not authorized to use this resource"),
         ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
     ])
     @PutMapping("/password/{id}")
     fun updatePassword(@RequestBody pass: String, @PathVariable id: Long) =
             handle4xx { vets.updatePassword(id, pass) }




}