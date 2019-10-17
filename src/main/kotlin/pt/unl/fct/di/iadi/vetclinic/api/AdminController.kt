package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import pt.unl.fct.di.iadi.vetclinic.services.PetNotFoundException

@Api(value = "VetClinic Management System - Admin API",
        description = "Management operations of ADMIN in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/user/admin")

class AdminController(val admin: AdminService) {
    @ApiOperation(value = "View a list of registered users", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved user list"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/users")
    fun getAllUsers() = admin.getAllUsers().map { UserDTO(it) }

    @GetMapping("/pets")
    fun getAllPets() = admin.getAllPets().map { PetDTO(it) }

    @GetMapping("/appointments")
    fun getAllAppointments() = admin.getAllAppointments().map { AppointmentDTO(it) }

    @GetMapping("/appointments/{id}")
    fun getAppointmentsByVetId(@PathVariable id: Long) =
            try {
                admin.getAppointmentsByVetId(id)
            } catch (e: NotFoundException) {
                throw NotFoundException(e.message ?: "Vet id not found")
            }
}