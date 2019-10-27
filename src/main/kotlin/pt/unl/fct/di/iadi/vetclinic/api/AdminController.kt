package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AdminDAO
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import pt.unl.fct.di.iadi.vetclinic.services.UserService


@Api(value = "VetClinic Management System - Admin API",
        description = "Management operations of ADMIN in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/user/admin")

class AdminController(val admin: AdminService) {

    @ApiOperation(value = "View a list of all employees", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved employees list"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/employees")
    fun getAllUsers() = admin.getAllEmployees().map { UserDTO(it) }



    @ApiOperation(value = "View a list of all pets", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved pets list"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/pets")
    fun getAllPets() = admin.getAllPets().map { PetDTO(it) }



    @ApiOperation(value = "View a list of all clients", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved clients list"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/clients")
    fun getAllClients() = admin.getAllClients().map { UserDTO(it) }



    @ApiOperation(value = "View a list of all appointments", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved appointments list"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/appointments")
    fun getAllAppointments() = admin.getAllAppointments().map { AppointmentDTO(it) }



    @ApiOperation(value = "View a list of appointments of a given vet id", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved vet appointments list"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/appointments/{id}")
    fun getAppointmentsByVetId(@PathVariable id: String) = handle4xx { admin.getAppointmentsByVetId(id).map { AppointmentDTO(it) } }



    @ApiOperation(value = "Fire employee with given id", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully fired employee with given id"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("/employees/{id}")
    fun fireEmployee(@PathVariable id: String) = handle4xx { admin.fireEmployee(id) }



    @ApiOperation(value = "Assign empty schedule to vet with given id", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully set vet schedule"),
        ApiResponse(code = 401, message = "You are not authorized to view this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("/schedule/{id}")
    fun setVetSchedule(@PathVariable id: String) = handle4xx { admin.setVetSchedule(id) }

}