package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.AppointmentService
import pt.unl.fct.di.iadi.vetclinic.services.PetService


@Api(value = "VetClinic Management System - Pet API",
        description = "Management operations of Pets in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/appointments")
class AppointmentController(val apts: AppointmentService, val pets: PetService) {

    @ApiOperation(value = "View a list of registered appointments", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllAppointments() = apts.getAllAppointments().map { AppointmentDTO(it) }

    @ApiOperation(value = "Add a new appointments", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added an appointments"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun addNewAppointment(@RequestBody apt: AppointmentDTO): AppointmentDTO =
            AppointmentDTO(apts.addNewAppointment(AppointmentDAO( apt, pets.getOnePet(apt.petID),pets.getOnePet(apt.petID).owner)))


    @ApiOperation(value = "Get the details of a single appointment by id", response = AppointmentDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved appointment details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}")
    fun getOneAppointment(@PathVariable id:Long) : AppointmentDTO =
            handle4xx { apts.getOneAppointment(id).let { AppointmentDTO(it) }  }

}