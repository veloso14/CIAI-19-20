package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentRepository

@Service
class AppointmentService(val appointment: AppointmentRepository) {

    fun getAllAppointment(): List<AppointmentDAO> = appointment.findAll().toList()

    fun addNewAppointment(pet: AppointmentDAO) {
        appointment.save(pet)
    }

    fun getOneAppointment(id: Long) =
            appointment.findById(id).orElseThrow { NotFoundException("There is no Appointment with Id $id") }


    fun deleteAppointment(id: Long) {
        val pet: AppointmentDAO = getOneAppointment(id)

        appointment.delete(pet)
    }
}