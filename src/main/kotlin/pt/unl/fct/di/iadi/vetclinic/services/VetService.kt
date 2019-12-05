package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.api.MonthDTO
import pt.unl.fct.di.iadi.vetclinic.api.UserPasswordDTO
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
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

    fun completeAppointment(id: Long, desc: String) {

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



    fun hireVet(vet: VetDAO) =
            when {
                vet.id != 0L -> throw PreconditionFailedException("Id must be 0 in insertion")
                users.findByUsername(vet.username).isPresent -> throw PreconditionFailedException("There is already an user with the specified username")
                else -> {vet.password = BCryptPasswordEncoder().encode(vet.password);users.save(vet)}
            }


    fun updateUser(id: Long, user: VetDAO) =
            getOneVet(id).let { it.update(user); users.save(it) }

    fun updatePassword(id: Long, password: UserPasswordDTO) = getOneVet(id).let { it.changePassword(BCryptPasswordEncoder().encode(password.password)); users.save(it) }


    fun fireVet(id: Long) = getOneVet(id).let { it.updateFrozen(true); users.save(it) }
    /* fun fireVet(id:Long){
        val user = getOneVet(id)
        if (user is VetDAO){
            user.updateFrozen(true)
            vets.save(user)
        }
    }

     */

    //
    // Get Schedule e Set schedule
    //

    // returns list of free slots in a month for all vet schedules
    fun getFreeSlots(month: String, day: Int, id: Long): List<SlotDAO> {
        val mon = getMonth(month)
        val vet = getOneVet(id)
        val monthFreeSlots = mutableListOf<SlotDAO>()
        val schedulesByMonth = schedulesRep.findByMonth(mon)
        schedulesByMonth.forEach { schedule ->
            if (schedule.vet == vet) {
                val shifts = schedule.shifts
                // shifts.forEach { shift ->
                val freeSlots = shifts[day - 1].getFreeSlots()
                monthFreeSlots.addAll(freeSlots)
                // }
            }
        }
        return monthFreeSlots.toList()
    }

    /*fun getSchedule1(id: Long, mon: String): ScheduleDAO {
        val vet = getOneVet(id)
        val month = getMonth(mon)
        val list = schedulesRep.findByVet(vet)
        for (sch in list) {
            if (sch.month == month) {
                println("contagem: ${sch.shifts.count()}")
                println("schedule maluco slots count: ${sch.shifts.count()}")
                return sch
            }
        }
        throw PreconditionFailedException("No schedule for that vet and month")
    }*/


    fun getSchedule(id: Long, mon: MonthDTO): ScheduleDAO {
        val vet = getOneVet(id)
        val month = getMonth(mon.month)
        val list = vet.schedules
        for (sch in list) {
            if (sch.month == month) {
                return sch
            }
        }
        throw PreconditionFailedException("No schedule for that vet and month")
    }

    fun getMonth(mon: String): Month {
        return when {
            mon.contains("JAN") -> Month.JANUARY
            mon.contains("FEB") -> Month.FEBRUARY
            mon.contains("MAR") -> Month.MARCH
            mon.contains("APR") -> Month.APRIL
            mon.contains("MAY") -> Month.MAY
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
    fun setSchedule(id: Long, mon: String) {
        val month = getMonth(mon)
        val vet = getOneVet(id)
        val schedules: List<ScheduleDAO> = vet.schedules

        // check for a vet if he already has schedule set for that month
        schedules.forEach() {
            if (it.month == month) {
                throw PreconditionFailedException("Schedule for that month already set!")
            }
        }

        /* TODO when creating new schedule is possible it's saving with the default constructor and
                not updating with the constructed shift list */
        // create new schedule for said vet and month
        val newSchedule = createSchedule(vet, month)

        // update vet.schedules with newly created schedule
        println("before updating schedule number: ${vet.schedules.size}") // 0
        vet.addSchedule(newSchedule)
        println("after updating schedule number: ${vet.schedules.size}") // 1

        /* TODO bug here: before saving schedulesRep size = 0 and after saving only 1 object
                schedulesRep size = 2 ??? */
        // add newly created schedule to all schedules rep
        println("before saving schedule: ${schedulesRep.count()}") // 0
        schedulesRep.save(newSchedule)
        println("after saving schedule: ${schedulesRep.count()}") // 2
    }

    // creates a schedule ( list of 30 or 31 shifts corresponding to each day of the month )
    fun createSchedule(vet: VetDAO, month: Month): ScheduleDAO {
        val shifts = mutableListOf<ShiftDAO>()
        val scheduleDAO = ScheduleDAO(vet, month)

        for (x in 1..month.length(false)) {
            val shift = ShiftDAO(scheduleDAO)
            shifts.add(createShift(shift, scheduleDAO, x))
        }
        scheduleDAO.updateShifts(shifts)
        return scheduleDAO
    }

    // creates a shift ( list of 16 slots of 30 min )
    fun createShift(shifts: ShiftDAO, schedules: ScheduleDAO, day: Int): ShiftDAO {
        val slots = mutableListOf<SlotDAO>()

        val year = 2019
        val month = schedules.month.value
        //val date = Date(year, month - 1, day, 9, 0)
        val dateTime = LocalDateTime.of(year, month, day, 9, 0)
        val instant = dateTime.atZone(ZoneId.of("Portugal")).toInstant()
        val date = Date.from(instant)


        for (x in 0..16) {
            val newDate = addMinutes(date, x * 30)
            val slot = SlotDAO(newDate, shifts)
            slots.add(slot)
        }
        //return ShiftDAO(slots.toList(), schedules)
        shifts.updateSlots(slots)
        return shifts
    }

    // uses date object to create date object with +30 minutes
    // used for slot creation
    fun addMinutes(date: Date, minutes: Int): Date {
        return Date(date.time + minutes * 60000)
    }


    fun getOneVetByUsername(username: String): VetDAO =
            vets.findByUsername(username)
                    .orElseThrow { NotFoundException("There is no Vet with username $username") }

}

