package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentRepository

@Service
class AppointmentService(val appointment: AppointmentRepository) {

    fun getAllAppointments(): List<AppointmentDAO> = appointment.findAll().toList()

    fun addNewAppointment(apt: AppointmentDAO) =
            // defensive programming
            if (apt.id != 0L)
                throw PreconditionFailedException("Id must be 0 in insertion")
            else
                appointment.save(apt)


    fun getOneAppointment(id: Long): AppointmentDAO =
            appointment.findById(id).orElseThrow { NotFoundException("There is no Appointment with Id $id") }


}