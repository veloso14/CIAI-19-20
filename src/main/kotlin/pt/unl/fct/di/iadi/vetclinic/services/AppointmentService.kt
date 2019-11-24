package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentRepository
import pt.unl.fct.di.iadi.vetclinic.model.ScheduleRepository
import java.time.Month
import java.util.*


@Service
class AppointmentService(val appointment: AppointmentRepository,
                         val scheduleRepository: ScheduleRepository) {

    fun getAllAppointments(): List<AppointmentDAO> = appointment.findAll().toList()

    fun addNewAppointment(apt: AppointmentDAO): AppointmentDAO {
        when {
            // defensive programming
            apt.id != 0L -> throw PreconditionFailedException("Id must be 0 in insertion")
            checkAvailable(apt) -> throw PreconditionFailedException("Not available at that time and day")
            else -> return appointment.save(apt)
        }
    }


    // checks if the apt date (day to find the shift in scheduleDAO, and month to find schedule for that month
    // from all schedules of a single vet for a given year). if apt date is on same day and same time return true
    // assuming apt.date will always respect the 9h start and 17h end with 30 min steps (9.30/10.00/10.30...)
    fun checkAvailable(apt: AppointmentDAO): Boolean {
        val vet = apt.vet
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = apt.date

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)

        val schedule = scheduleRepository.findByVetAndMonth(vet, Month.of(month)).get()
        val shift = schedule.shifts[day]

        val freeSlots = shift.getFreeSlots()
        val calendarShift: Calendar = Calendar.getInstance()

        for (slot in freeSlots) {
            calendarShift.time = slot.start
            val sameDay = calendar.get(Calendar.DAY_OF_YEAR) == calendarShift.get(Calendar.DAY_OF_YEAR) &&
                    calendar.get(Calendar.YEAR) == calendarShift.get(Calendar.YEAR)

            val sameTime = calendar.get(Calendar.HOUR) == calendarShift.get(Calendar.HOUR) &&
                    calendar.get(Calendar.MINUTE) == calendarShift.get(Calendar.MINUTE)

            if (sameDay && sameTime)
                return true
        }

        return false
    }


    fun getOneAppointment(id: Long): AppointmentDAO =
            appointment.findById(id).orElseThrow { NotFoundException("There is no Appointment with Id $id") }


}