package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import pt.unl.fct.di.iadi.vetclinic.services.UserService


@Api(value = "VetClinic Management System - Admin API",
        description = "Management operations of ADMIN in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/user/admin")

class AdminController(val admin: AdminService, val users: UserService) {
    @ApiOperation(value = "View a list of registered users", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved user list"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/employees")
    fun getAllUsers() = admin.getAllEmployees().map { UserDTO(it) }

    @GetMapping("/pets")
    fun getAllPets() = admin.getAllPets().map { PetDTO(it) }

    @GetMapping("/clients")
    fun getAllClients() = admin.getAllClients().map { UserDTO(it) }

    @GetMapping("/appointments")
    fun getAllAppointments() = admin.getAllAppointments().map { AppointmentDTO(it) }

    //  @GetMapping("/appointments/{id}")
    //  fun getAppointmentsByVetId(@PathVariable id: Long) = handle404 {admin.getAppointmentsByVetId(id).map { AppointmentDTO(it)  }}

}