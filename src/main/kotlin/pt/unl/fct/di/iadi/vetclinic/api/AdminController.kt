package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AdminDAO
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import java.time.Month


@Api(value = "VetClinic Management System - Admin API",
        description = "Management operations of Admins in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/admins")
class AdminController(val admins: AdminService) {


    //@PreAuthorize("hasAnyRole('ROLE_ADMIN') and @securityService.canEditAdmin(principal, #id)")
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


    //Aqui podem todos mesmo nem estando registado
    @ApiOperation(value = "View a list of registered admins", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @GetMapping("")
    fun getAllAdmins() = admins.getAllAdmins().map { AdminDTO(it) }



    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Hire new admin", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully added a admin"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PostMapping("")
    fun addNewAdmin(@RequestBody admin: AdminDTO): AdminDTO =
            AdminDTO(admins.hireAdmin(AdminDAO(admin)))


    /*
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Fire a vet", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully fired a vet"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/vets/{id}")
    fun fireVet(@PathVariable id: Long) =
            handle4xx { admins.fireVet(id)}

     */

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Fire a admin", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully fired a admin"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @DeleteMapping("/{id}")
    fun deleteAdmin(@PathVariable id: Long) =
            handle4xx { admins.fireAdmin(id) }


    //@PreAuthorize("hasRole('ROLE_ADMIN') and @securityService.canEditAdmin(principal, #id)")
    @ApiOperation(value = "Update contact info of a admin", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated a user"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}/info")
    fun updateAdmin(@RequestBody user: AdminDTO, @PathVariable id: Long) =
            handle4xx { admins.updateUser(id,AdminDAO(user)) }

    //@PreAuthorize("hasRole('ROLE_ADMIN') and @securityService.canEditAdmin(principal, #id)")
    @ApiOperation(value = "Change the password of a admin", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully changed the password"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}/password")
    fun updatePassword(@RequestBody pass: String, @PathVariable id: Long) =
            handle4xx { admins.updatePassword(id, pass) }





}