package pt.unl.fct.di.iadi.vetclinic.api


import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import pt.unl.fct.di.iadi.vetclinic.services.AppointmentService

//import pt.unl.fct.di.iadi.vetclinic.services.UserService

@RestController
@RequestMapping("/users")
@Controller
@ResponseBody

class AppointmentController(val appointment: AppointmentService) {


}