package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@Service
class VetService(val vets: VetRepository,
                 val appointments: AppointmentRepository,
                 val pets: PetRepository,
                 val clients: ClientRepository,
                 val users: UserRepository,
                 val schedulesRep: ScheduleRepository) {

    fun getAllVets(): List<VetDAO> = vets.findAllByFrozenFalse().toList()

    fun getOneVet(id: Long): VetDAO =
            vets.findById(id)
                    .orElseThrow { NotFoundException("There is no vet with Id $id") }

    fun completeAppointment(id:Long, desc:String){

        val apt = appointments.findById(id)
                .orElseThrow { NotFoundException("There is no Appointment with Id $id") }

        apt.complete(desc)
        appointments.save(apt)
    }



    /*
    fun scheduleOfVet(name: String): VetScheduleDAO {

        val schedule = schedules.findByVetId(name).orElseThrow { NotFoundException("There is no Pet with Id $name") }
        return schedule // This redirection has pre-fetching
    }
*/


    fun appointmentsOfVet(id: Long): List<AppointmentDAO> {
        val vet = vets.findByIdWithAppointment(id)
                .orElseThrow { NotFoundException("There is no Pet with Id $id") }

        return vet.appointments // This redirection has pre-fetching
    }

/*
    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllClients(): List<ClientDAO> = clients.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();

 */

    fun hireVet(vet:VetDAO) =
            when {
                vet.id != 0L -> throw PreconditionFailedException("Id must be 0 in insertion")
                users.findByUsername(vet.username).isPresent -> throw PreconditionFailedException("There is already an user with the specified username")
                else -> vets.save(vet)
            }


    fun updateUser(id: Long, user: VetDAO) =
            getOneVet(id).let { it.update(user); vets.save(it) }

    fun updatePassword(id: Long, password: String) = getOneVet(id).let { it.changePassword(password); vets.save(it) }

    //
    // Get Schedule e Set schedule
    //

    fun getSchedule(id: Long, mon: String): ScheduleDAO {
        val vet = getOneVet(id)
        val month = getMonth(mon)
        val list = schedulesRep.findByVet(vet)
        for (sch in list) {
            if (sch.month == month)
                return sch
        }
        throw PreconditionFailedException("No schedule for that vet and month")
    }

    fun getMonth(mon: String): Month {
        return when {
            mon.contains("JAN") -> Month.JANUARY
            mon.contains("FEB") -> Month.FEBRUARY
            mon.contains("MAR") -> Month.MARCH
            mon.contains("APR") -> Month.APRIL
            mon.contains("JUN") -> Month.JUNE
            mon.contains("JUL") -> Month.JULY
            mon.contains("AUG") -> Month.AUGUST
            mon.contains("SEP") -> Month.SEPTEMBER
            mon.contains("OCT") -> Month.OCTOBER
            mon.contains("NOV") -> Month.NOVEMBER
            mon.contains("DEC") -> Month.DECEMBER
            else -> throw PreconditionFailedException("Month id not found!")
        }
    }

    // gets one vet, creates default empty schedule and updates vet
    fun setSchedule(id: Long, mon: String): ScheduleDAO {
        val month = getMonth(mon)
        val vet = getOneVet(id)
        val schedules = vet.schedules

        schedules.forEach() {
            if (it.month == month) {
                throw PreconditionFailedException("Schedule for that month already set!")
            }
        }

        val schedule = createSchedule(ScheduleDAO(vet, month))
        val sch = mutableListOf<ScheduleDAO>()
        sch.addAll(schedules)
        sch.add(schedule)
        vet.updateSchedules(sch.toList())
        schedulesRep.save(schedule)
        return schedule
    }

    // creates a schedule ( list of 30 or 31 shifts corresponding to each day of the month )
    fun createSchedule(schedules: ScheduleDAO): ScheduleDAO {
        val shifts = mutableListOf<ShiftDAO>()
        val shift = ShiftDAO(schedules)

        for (x in 1..schedules.month.length(false)) {
            shifts.add(createShift(shift, schedules, x))
        }
        return ScheduleDAO(schedules.vet, schedules.month, shifts)
    }

    // creates a shift ( list of 16 slots of 30 min )
    fun createShift(shifts: ShiftDAO, schedules: ScheduleDAO, day: Int): ShiftDAO {
        val slots = mutableListOf<SlotDAO>()

        val year = 2019
        val month = schedules.month.value
        val date = Date(year, month-1, day, 9, 0)

        for (x in 0..16) {
            val newDate = addMinutes(date, x*30)
            val slot = SlotDAO(newDate, shifts)
            slots.add(slot)
        }
        return ShiftDAO(slots, schedules)
    }

    fun addMinutes(date: Date, minutes: Int): Date {
        return Date(date.time + minutes * 60000)
    }

}