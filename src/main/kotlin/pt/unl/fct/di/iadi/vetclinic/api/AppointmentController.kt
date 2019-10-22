package pt.unl.fct.di.iadi.vetclinic.api


import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.iadi.vetclinic.services.AppointmentService
//import pt.unl.fct.di.iadi.vetclinic.services.UserService

@RestController
@RequestMapping("/users")
@Controller
@ResponseBody

class AppointmentController(val appointment: AppointmentService){



}