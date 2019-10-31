package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.services.UserService


@Api(value = "VetClinic Management System - User API",
        description = "Management operations of Users in the IADI 2019 Pet Clinic")
@RestController
@RequestMapping("/users")
class UserController(val users: UserService) {

/*
    @ApiOperation(value = "Get the details of a single user by id", response = UserDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved user details"),
        ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    ])
    @GetMapping("/{id}")
    fun getOnePet(@PathVariable id:Long) : UserDTO =
            handle4xx { users.getOneUser(id).let { UserDTO(it) } }

    @ApiOperation(value = "Update a user", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated a user"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}")
    fun updatePet(@RequestBody user: UserDTO, @PathVariable id: Long) =
            handle4xx { users.updateUser(UserDAO(user), id) }

   /* @ApiOperation(value = "Change the password of a user", response = Unit::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully changed the password"),
        ApiResponse(code = 401, message = "You are not authorized to use this resource"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    ])
    @PutMapping("/{id}")
    fun updatePassword(@RequestBody pass: String, @PathVariable id: Long) =
            handle4xx { users.updatePassword(id, pass) }

    */

 */


}

