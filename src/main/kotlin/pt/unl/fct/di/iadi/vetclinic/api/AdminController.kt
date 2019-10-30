package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AdminDAO
import pt.unl.fct.di.iadi.vetclinic.model.VetDAO
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import pt.unl.fct.di.iadi.vetclinic.services.VetService


@Api(value = "VetClinic Management System - Admin API",
        description = "Management operations of Admins in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/admins")
class AdminController(val admins: AdminService) {



    @ApiOperation(value = "Get the details of a single admin by id", response = AdminDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved admin details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}")
    fun getOneAdmin(@PathVariable id:Long) : AdminDTO =
            handle4xx { admins.getOneAdmin(id).let { AdminDTO(it) } }




    @ApiOperation(value = "View a list of registered pets", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/pets")
    fun getAllPets() = admins.getAllPets().map { PetDTO(it) }

    @ApiOperation(value = "View a list of registered clients", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/clients")
    fun getAllClients() = admins.getAllClients().map { ClientDTO(it) }

    @ApiOperation(value = "View a list of registered appointments", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/appointments")
    fun getAllAppointments() = admins.getAllAppointments().map { AppointmentDTO(it) }

    @ApiOperation(value = "View a list of registered admins", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllAdmins() = admins.getAllAdmins().map { AdminDTO(it) }

    @ApiOperation(value = "View a list of registered vets", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/vets")
    fun getAllVets() = admins.getAllVets().map { VetDTO(it) }

    @ApiOperation(value = "Hire new vet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a vet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("/vets")
    fun addNewVet(@RequestBody vet: VetDTO): VetDTO =
            VetDTO(admins.hireVet(VetDAO(vet, emptyList())))

    @ApiOperation(value = "Hire new admin", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a admin"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun addNewAdmin(@RequestBody admin: AdminDTO): AdminDTO =
            AdminDTO(admins.hireAdmin(AdminDAO(admin)))

    @ApiOperation(value = "Fire a vet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully fired a vet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/vets/{id}")
    fun fireVet(@PathVariable id: Long) =
            handle4xx { admins.fireVet(id)}

    @ApiOperation(value = "Fire a admin", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully fired a admin"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @DeleteMapping("/{id}")
    fun deletePet(@PathVariable id: Long) =
            handle4xx { admins.fireAdmin(id) }

    @ApiOperation(value = "List the appointments related to a Vet", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved the list of appointments"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/vets/{id}/appointments")
    fun appointmentsOfVet(@PathVariable id: Long): List<AppointmentDTO> =
            handle4xx { admins.getVetsAppointments(id).map { AppointmentDTO(it) } }


}