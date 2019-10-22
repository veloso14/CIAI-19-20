package pt.unl.fct.di.iadi.vetclinic.api

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.model.AdminDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.model.VetDAO
import pt.unl.fct.di.iadi.vetclinic.services.UserService

@RestController
@RequestMapping("/users")
@Controller
@ResponseBody

class UserController(val userService: UserService){

    @ApiOperation("Register um novo USER" , response = UserDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso"),
        ApiResponse( code = 401 , message = "Falhou")
    ])
    @PostMapping("/register")
    fun register(@RequestBody user:UserDAO) {
        userService.addNewUser(user)
    }

    @ApiOperation("Registar Veterinário" , response = UserDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso"),
        ApiResponse( code = 401 , message = "Falhou"),
        ApiResponse( code = 403 , message = "Proibido")
    ])
    @PostMapping("/register/vet")
    fun registerVet(@RequestBody user:VetDAO) {
        userService.addNewUser(user)
    }

    @ApiOperation("Registar Cliente" , response = UserDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso"),
        ApiResponse( code = 401 , message = "Falhou"),
        ApiResponse( code = 403 , message = "Proibido")
    ])
    @PostMapping("/register/client")
    fun registerClient(@RequestBody user:ClientDAO) {
        userService.addNewUser(user)
    }

    @ApiOperation("Registo Admin" , response = UserDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso"),
        ApiResponse( code = 401 , message = "Falhou"),
        ApiResponse( code = 403 , message = "Proibido")
    ])
    @PostMapping("/register/admin")
    fun registerAdmin(@RequestBody user:AdminDAO) {
        userService.addNewUser(user)
    }

    @ApiOperation("Delete USER account" , response = UserDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso"),
        ApiResponse( code = 401 , message = "Falhou"),
        ApiResponse( code = 403 , message = "Proibido")
    ])
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id:String) {
        userService.deleteUser(id)
    }


    @ApiOperation("Login USER" , response = UserDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso"),
        ApiResponse( code = 401 , message = "Falhou"),
        ApiResponse( code = 403 , message = "Proibido")
    ])
    @GetMapping("/{id}{password}")
    //tenho duvida aqui
    fun login(@PathVariable id:Number, @PathVariable password:String) {

    }


    @ApiOperation("Faz logout do utilizador com o ID fornecido" , response = String::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso"),
        ApiResponse( code = 404 , message = "Utilizador não encontrado")
    ])
    @GetMapping("/logout/{id}")

    fun logout(@PathVariable id:Number){


    }

}