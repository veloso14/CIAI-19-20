package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.services.ClientService


@Api(value = "VetClinic Management System - Client API",
        description = "Management operations of Clients in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/clients")

class ClientController(val clients: ClientService) {


    @ApiOperation(value = "Get the details of a single client by id", response = ClientDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved client details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}")
    fun getOneClient(@PathVariable id: Long): ClientDTO =
            handle4xx { clients.getOneClient(id).let { ClientDTO(it) } }

     @ApiOperation(value = "List the appointments related to a Client", response = List::class)
 @ApiResponses(value = [
     ApiResponse(code = 200, message = "Successfully retrieved the list of appointments"),
     ApiResponse(code = 401, message = "You are not authorized to view the resource"),
     ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
     ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
 ])
 @GetMapping("/{id}/appointments")
 fun appointmentsOfClient(@PathVariable id: Long): List<AppointmentDTO> =
         handle4xx { clients.appointmentsOfClient(id).map { AppointmentDTO(it) } }



/*
    @ApiOperation(value = "Add a new appointment to a pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added an appointment to a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @PostMapping("/{name}/appointments")
    fun newAppointment(@PathVariable id: Long,
                       @RequestBody apt:AppointmentDTO,@RequestBody pet:PetDTO) =
            handle4xx {
                AppointmentDTO(clients.newAppointment(AppointmentDAO(apt, PetDAO(pet, emptyList()),clients.getOneClient(id))))
            }
*/






}