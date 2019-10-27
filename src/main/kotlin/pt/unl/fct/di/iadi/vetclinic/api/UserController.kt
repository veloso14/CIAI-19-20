package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.BlackListService
import pt.unl.fct.di.iadi.vetclinic.services.UserService
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/users")
@Controller
@ResponseBody

class UserController(val userService: UserService, val blackListService: BlackListService) {


    @ApiOperation("Register um novo USER", response = UserDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucesso"),
        ApiResponse(code = 401, message = "Falhou")
    ])

    @PostMapping("/register")
    fun register(@RequestBody user: UserDAO) {
        userService.addNewUser(user)
    }

    @ApiOperation("Registar Veterinário", response = UserDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucesso"),
        ApiResponse(code = 401, message = "Falhou"),
        ApiResponse(code = 403, message = "Proibido")
    ])

    @PreAuthorize("hasRole({'ADMIN'})")
    @PostMapping("/register/vet")
    fun registerVet(@RequestBody user: VetDAO) {
        userService.addNewUser(user)
    }

    @ApiOperation("Registar Cliente", response = UserDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucesso"),
        ApiResponse(code = 401, message = "Falhou"),
        ApiResponse(code = 403, message = "Proibido")
    ])
    @PreAuthorize("hasRole({'ADMIN'})")
    @PostMapping("/register/client")
    fun registerClient(@RequestBody user: ClientDAO) {
        userService.addNewUser(user)
    }

    @ApiOperation("Registo Admin", response = UserDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucesso"),
        ApiResponse(code = 401, message = "Falhou"),
        ApiResponse(code = 403, message = "Proibido")
    ])
    @PostMapping("/register/admin")
    fun registerAdmin(@RequestBody user: AdminDAO) {
        userService.addNewUser(user)
    }

    @ApiOperation("Delete USER account", response = UserDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucesso"),
        ApiResponse(code = 401, message = "Falhou"),
        ApiResponse(code = 403, message = "Proibido")
    ])

    @PreAuthorize("hasRole({'ADMIN','VETERINARIO'})")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String) {
        userService.deleteUser(id)
    }


    @ApiOperation("Get USER account", response = UserDTO::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucesso"),
        ApiResponse(code = 401, message = "Falhou"),
        ApiResponse(code = 403, message = "Proibido")
    ])

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String) = userService.findUser(id).map { UserDTO(it) }

    @ApiOperation("Obtem todos os utilizadores é necessário ser admin", response = String::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucesso"),
        ApiResponse(code = 404, message = "Utilizador não encontrado")
    ])
    @PreAuthorize("hasRole({'ADMIN'})")
    @GetMapping("/all")
    fun getAll() = userService.getAllUser().map { UserDTO(it.id, it.name, it.email , it.username , it.password , it.cellphone , it.address ) }

    @ApiOperation("Faz logout do utilizador com o ID fornecido", response = String::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Sucesso"),
        ApiResponse(code = 404, message = "Utilizador não encontrado")
    ])
    @PreAuthorize("hasRole({'ADMIN', 'USER' , 'VETERINARIO'})")
    @PostMapping("/logout")
    fun logout(@RequestBody token: BackListDAO) {
        handle200{
            blackListService.addKey(token)
        }

    }

}