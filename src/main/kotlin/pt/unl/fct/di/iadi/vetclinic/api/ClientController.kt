package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.ClientService
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import pt.unl.fct.di.iadi.vetclinic.services.VetService


@Api(value = "VetClinic Management System - Client API",
        description = "Management operations of Clients in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/clients")

//???
class ClientController(val clients: ClientService, val pets:PetService, val vets: VetService) {


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




    @ApiOperation(value = "Book an appointment", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added an appointment to a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @PostMapping("/{id}/appointments")
    fun newAppointment(@PathVariable id: Long,
                       @RequestBody apt:AppointmentDTO,@RequestBody pet:PetDTO) =
            handle4xx {
                //aqui obter lista dos apts do pet???
               AppointmentDTO(clients.newAppointment(AppointmentDAO(apt, pets.getOnePet(pet.id), clients.getOneClient(id), vets.getOneVet(apt.vetID))))
           // pets.newAppointment(pet.id,apt)
            }

    @ApiOperation(value = "List the pets related to a client", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of pets"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}/pets")
    fun petsOfClient(@PathVariable id:Long): List<PetDTO> =
            handle4xx {
                clients.petsOfClient(id)
                        .map { PetDTO(it) }
            }

    @ApiOperation(value = "Add a new pet to a client", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a pet to a client"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @PostMapping("/{id}/pets")
    fun newPet(@PathVariable id:Long,
                       @RequestBody pet:PetDTO) =
            handle4xx {
                PetDTO(clients.newPet(PetDAO(pet, pets.getOnePet(pet.id).appointments,clients.getOneClient(id))))
            }






}