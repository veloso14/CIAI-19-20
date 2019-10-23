package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.ClientService


@Api(value = "VetClinic Management System - Client API",
        description = "Management operations of Clients in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/user/client")

class ClientController(val clients: ClientService) {

    @GetMapping("/{name}/appointments")
    fun appointmentsOfClient(@PathVariable name: String): List<AppointmentDTO> =
            handle4xx { clients.appointmentsOfClient(name).map { AppointmentDTO(it) } }


    @GetMapping("/{name}")
    fun getOneClient(@PathVariable name: String): ClientDTO =
            handle4xx { clients.getOneClient(name).let { ClientDTO(it) } }


    @ApiOperation(value = "Add a new appointment to a pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added an appointment to a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @PostMapping("/{name}/appointments")
    fun newAppointment(@PathVariable name: String,
                       @RequestBody apt:AppointmentDTO) =
            handle4xx {
                AppointmentDTO(clients.newAppointment(AppointmentDAO(apt, PetDAO(),clients.getOneClient(name))))
            }

   /* @ApiOperation(value = "View a list of registered clients", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllClients() : List<ClientPetsDTO> =
            clients.getAllClients().map { ClientPetsDTO(ClientDTO(it),
                    it.pets.map { PetDTO(it) }) }
*/

    @ApiOperation(value = "List the pets related to a Client", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of pets"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{name}/pets")
    fun petsOfClient(@PathVariable name: String): List<PetDTO> =
            handle4xx { clients.petsOfClient(name).map { PetDTO(it) } }

    @ApiOperation(value = "Add a new pet to a client", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added an appointment to a client"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @PostMapping("/{name}/pets")
    fun newPet(@PathVariable name:String,
               @RequestBody pet: PetDTO) =
            handle4xx {
                PetDTO(clients.newPet(PetDAO(pet, emptyList(), emptyList(),clients.getOneClient(name))))
            }




    @ApiOperation(value = "Delete a client", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted a client"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @DeleteMapping("/{name}")
    fun deletePet(@PathVariable name: String) =
            handle4xx { clients.deleteClient(name) }
}