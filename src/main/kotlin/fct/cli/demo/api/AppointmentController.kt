package fct.cli.demo.api

import fct.cli.demo.dto.AppointmentDTO
import fct.cli.demo.dto.PetDTO
import fct.cli.demo.services.AppointmentService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/appointment")
@Controller
@ResponseBody

class AppointmentController(val service: AppointmentService){



    @ApiOperation("Get appointemnte by ID   " , response = AppointmentDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso")
    ])
    @GetMapping("/{id}")
    fun getAppointment(@PathVariable id:Number) {
        //Fazer
    }


    @ApiOperation("Get appointemnt by ID   " , response = AppointmentDTO::class)
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso")
    ])
    @PostMapping("/")
    fun createAppointment(@RequestBody appointment:AppointmentDTO) {
        //Fazer
    }


    @ApiOperation("Remove appointemnt")
    @ApiResponses( value = [
        ApiResponse( code = 200 , message = "Sucesso")
    ])
    @DeleteMapping("/{id}")
    fun deleteAppointment(@PathVariable id:Number): String {


        return "Done!"
    }



}