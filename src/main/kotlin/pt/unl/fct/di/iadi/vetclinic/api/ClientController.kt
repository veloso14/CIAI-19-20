package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.ClientService
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import pt.unl.fct.di.iadi.vetclinic.services.VetService


@Api(value = "VetClinic Management System - Client API",
        description = "Management operations of Clients in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/clients")

class ClientController(val clients: ClientService, val pets:PetService, val vets: VetService) {


    @PreAuthorize("hasAnyRole('ROLE_CLIENT' , 'ROLE_USER')")
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_VET')")
    @ApiOperation(value = "View a list of registered clients", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllClients() = clients.getAllClients().map { ClientDTO(it) }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
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



    @ApiOperation(value = "New client", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a client"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun addNewClient(@RequestBody client: ClientDTO): ClientDTO =
            ClientDTO(clients.newClient(ClientDAO(client, emptyList(), emptyList())))



    @PreAuthorize("hasRole('ROLE_CLIENT')")
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
               AppointmentDTO(clients.newAppointment(AppointmentDAO(apt, pets.getOnePet(pet.id), clients.getOneClient(id), vets.getOneVet(apt.vetID))))
            }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
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

    @PreAuthorize("hasRole('ROLE_CLIENT')")
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
                val onePet = pets.getOnePet(id)
                PetDTO(clients.newPet(PetDAO(pet, onePet.appointments,clients.getOneClient(id))))
            }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "Delete a pet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully deleted a pet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/pets/{id}")
    fun deletePet( @PathVariable id: Long) =
            handle4xx { clients.deletePet(id) }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "Update contact info of a client", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated a user"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}/info")
    fun updateClient(@RequestBody user: ClientDTO, @PathVariable id: Long) =
            handle4xx { clients.updateUser(id, ClientDAO(user, emptyList(), emptyList())) }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
     @ApiOperation(value = "Change the password of a client", response = Unit::class)
     @ApiResponses(value = [
         ApiResponse(code = 200, message = "Successfully changed the password"),
         ApiResponse(code = 401, message = "You are not authorized to use this resource"),
         ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
     ])
     @PutMapping("/{id}/password")
     fun updatePassword(@RequestBody pass: String, @PathVariable id: Long) =
             handle4xx { clients.updatePassword(id, pass) }






}