package pt.unl.fct.di.iadi.vetclinic.api


import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.services.UnregisteredUserService


@Api(value = "VetClinic Management System - UnregisteredUser API",
        description = "Management operations of UnregisteredUsers in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("")
class UnregisteredUserController(val unregisteredUsers: UnregisteredUserService) {




    @ApiOperation(value = "View a list of registered admins", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllAdmins() = unregisteredUsers.getAllAdmins().map { AdminDTO(it) }

    @ApiOperation(value = "View a list of registered vets", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("/vets")
    fun getAllVets() = unregisteredUsers.getAllVets().map { VetDTO(it) }




}