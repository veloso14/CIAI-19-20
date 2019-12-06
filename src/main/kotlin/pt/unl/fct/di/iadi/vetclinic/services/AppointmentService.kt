package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*
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
            !checkAvailable(apt) -> throw PreconditionFailedException("Not available at that time and day")
            else -> return appointment.save(apt)
        }
    }


    fun getScheduleForAppointment(apt: AppointmentDAO): ScheduleDAO {
        val vet = apt.vet
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = apt.date
        val aptMonth = Month.of(calendar.get(Calendar.MONTH) + 1)
        println("aptMonth: $aptMonth")
        val schedulesByVet = scheduleRepository.findByVet(vet)

        println("schedules by vet size: ${schedulesByVet.count()}")

        when {
            schedulesByVet.isEmpty() -> throw PreconditionFailedException("Vet with Id:${vet.id} doesn't have any schedules")
            else -> {
                schedulesByVet.forEach { schedule ->
                    if (schedule.month == aptMonth) {
                        println("month equals.")
                        return schedule
                    }
                }
                throw PreconditionFailedException("There are no schedules set for that month and vet combo")
            }

        }
    }

    fun getFreeSlots(slots: List<SlotDAO>): List<SlotDAO> {
        val list = mutableListOf<SlotDAO>()
        for (slot in slots) {
            if (slot.available) {
                list.add(slot)
            }
        }
        return list.toList()
    }

    // checks if the apt date (day to find the shift in scheduleDAO, and month to find schedule for that month
    // from all schedules of a single vet for a given year). if apt date is on same day and same time return true
    // assuming apt.date will always respect the 9h start and 17h end with 30 min steps (9.30/10.00/10.30...)
    fun checkAvailable(apt: AppointmentDAO): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = apt.date
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val schedule = getScheduleForAppointment(apt)
        val shift = schedule.shifts[day - 1]

        val slots = shift.slots
        val freeSlots = getFreeSlots(slots)

        val calendarShift: Calendar = Calendar.getInstance()

        for (slot in freeSlots) {
            calendarShift.time = Date(slot.start)
            val sameDay = calendar.get(Calendar.DAY_OF_MONTH) == calendarShift.get(Calendar.DAY_OF_MONTH) &&
                    calendar.get(Calendar.MONTH) == calendarShift.get(Calendar.MONTH)

            val sameTime = calendar.get(Calendar.HOUR) == calendarShift.get(Calendar.HOUR) &&
                    calendar.get(Calendar.MINUTE) == calendarShift.get(Calendar.MINUTE)

            if (sameDay && sameTime) {
                slot.setAvailableFalse()
                return true
            }
        }

        return false

    }


    fun getOneAppointment(id: Long): AppointmentDAO =
            appointment.findById(id).orElseThrow { NotFoundException("There is no Appointment with Id $id") }


}