//package pt.unl.fct.di.iadi.vetclinic.api
//
//
//import io.swagger.annotations.ApiOperation
//import io.swagger.annotations.ApiResponse
//import io.swagger.annotations.ApiResponses
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.*
//import pt.unl.fct.di.iadi.vetclinic.services.UserService
//
//@RestController
//@RequestMapping("/users")
//@Controller
//@ResponseBody
//
//class UserController(val service: UserService){
//
//    @ApiOperation("Register USER" , response = List::class)
//    @ApiResponses( value = [
//        ApiResponse( code = 200 , message = "Sucesso")
//    ])
//    @PostMapping("/register")
//    fun register(@RequestBody pet:UserDTO) {
//
//
//    }
//
//    @ApiOperation("Login USER" , response = UserDTO::class)
//    @ApiResponses( value = [
//        ApiResponse( code = 200 , message = "Sucesso")
//    ])
//    @GetMapping("/{id}")
//    fun login(@PathVariable id:Number) {
//
//    }
//
//
//    @ApiOperation("Retrives info about pet with id id" , response = UserDTO::class)
//    @ApiResponses( value = [
//        ApiResponse( code = 200 , message = "Sucesso")
//    ])
//    @GetMapping("/logout/{id}")
//    fun logout(@PathVariable id:Number){
//
//    }
//
//}