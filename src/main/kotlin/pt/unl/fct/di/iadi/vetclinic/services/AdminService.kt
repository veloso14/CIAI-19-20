package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

@Service
class AdminService(val admins: AdminRepository,
                 val appointments: AppointmentRepository,
                 val pets: PetRepository,
                 val clients: ClientRepository,
                 val vets: VetRepository,
                 val users: UserRepository,
                 val vetService: VetService  ) {

    fun getOneAdmin(id: Long): AdminDAO =
            admins.findById(id)
                    .orElseThrow { NotFoundException("There is no vet with Id $id") }

    fun getAllAdmins(): List<AdminDAO> = admins.findAll().toList()

    /*
    fun hireVet(vet:VetDAO) =
        if (vet.id != 0L)
            throw PreconditionFailedException("Id must be 0 in insertion")
        else vets.save(vet)
     */

  /*  fun hireAdmin(admin:AdminDAO) {
        val aUser = users.findByUsername(admin.username)
         if ( aUser.isPresent ) throw PreconditionFailedException("There is already an user with the specified username")
        else {
            //vet.password = BCryptPasswordEncoder().encode(user.password)
            admin.password = admin.password
            admins.save(admin)
        }
    }
   */

    fun hireAdmin(admin:AdminDAO) =
            when {
                admin.id != 0L -> throw PreconditionFailedException("Id must be 0 in insertion")
                users.findByUsername(admin.username).isPresent -> throw PreconditionFailedException("There is already an user with the specified username")
                else -> admins.save(admin)
            }

    fun findEmployee(id: Long): UserDAO = users.findById(id).orElseThrow { NotFoundException("There is no user with Id $id") }

    fun fireVet(id:Long){
        val user = findEmployee(id)
        if (user is VetDAO){
            user.updateFrozen(true)
            vets.save(user)
        }
    }

    fun fireAdmin(id:Long){
        if(id != 1L){
            val user = findEmployee(id)
          if (user is AdminDAO)
            users.delete(user)
        }
        else
            throw PreconditionFailedException ("You're not able to remove the default account")
    }

   /* fun getVetsAppointments(id:Long): List<AppointmentDAO>{
        return vetService.appointmentsOfVet(id)

    }
    */
    // gets one vet, creates default empty schedule and updates vet
    fun setSchedule(id: Long, month: Month) {
       val vet = vetService.getOneVet(id)
       val schedule = createSchedule(ScheduleDAO(vet, month), month, vet)
       val schedules = vet.schedules
       schedules.add(schedule)
       vet.updateSchedules(schedules)
   }

    // creates a schedule ( list of 30 or 31 shifts corresponding to each day of the month )
    fun createSchedule(schedules: ScheduleDAO, month: Month, vet: VetDAO): ScheduleDAO {
        val schedule = mutableListOf<ShiftDAO>()
        val shift = ShiftDAO(schedules)
        for (x in 1..month.length(false)) {
            schedule.add(createShift(shift, schedules))
        }
        return ScheduleDAO(vet, month, schedule)
    }

    // creates a shift ( list of 16 slots of 30 min )
    fun createShift( shifts: ShiftDAO , schedules: ScheduleDAO): ShiftDAO{
        val shift = mutableListOf<SlotDAO>()
        for (x in 1..16) {
            val slot = SlotDAO(LocalDateTime.now(), shifts)
            shift.add(slot)
        }
        return ShiftDAO(shift, schedules)
    }


    fun updateUser(id: Long, user: AdminDAO) =
            getOneAdmin(id).let { it.update(user); admins.save(it) }

    fun updatePassword(id: Long, password: String) = getOneAdmin(id).let { it.changePassword(password); admins.save(it) }


}