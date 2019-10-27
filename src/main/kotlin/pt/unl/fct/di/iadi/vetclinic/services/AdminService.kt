package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*


@Service
class AdminService(
        val pets: PetRepository,
        val clients: ClientRepository,
        val appointments: AppointmentRepository,

        val users: UserRepository
     //   val schedules: VetScheduleRepository

) {
    // val logger = LoggerFactory.getLogger(AdminService::class.java)

    fun getAllEmployees(): List<UserDAO> = users.findAll().toList()

    fun getAllPets(): List<PetDAO> = pets.findAll().toList()

    fun getAllClients(): List<UserDAO> = clients.findAll().toList()

    fun getAllAppointments(): List<AppointmentDAO> = appointments.findAll().toList();

    fun findEmployee(id: String): UserDAO = users.findById(id).orElseThrow { NotFoundException("There is no user with Id $id") }

    // if employee is admin remove account; if employee is vet freeze account
    fun fireEmployee(id:String) {
        val user = findEmployee(id)
        if (user is AdminDAO) {
            users.deleteById(id)
        } else if (user is VetDAO) {
            user.updateFrozen(true)
        }
    }

    // returns the list of appointments of a single Vet by giving his unique Id
    fun getAppointmentsByVetId(id: String): MutableList<AppointmentDAO> {
        val vet = findEmployee(id)
        var appointments = mutableListOf<AppointmentDAO>()
        if (vet is VetDAO) {
            appointments = vet.appointments
        }
        return appointments
    }

    fun setVetSchedule(id: String) {
        val vet = findEmployee(id)
        if (vet is VetDAO) {
            // check first if said vet already has a schedule created and throw exception if that's the case TODO
            // val existingSchedule = schedules.findById(...)
            // if (existingSchedule isPresent) throw Exception
            val schedule = createSchedule(vet)
            val vetSchedule = VetScheduleDAO(0L, vet, schedule)
           // schedules.save(vetSchedule)


        } else {
            throw NotFoundException("Vet with given Id:${id} doesn't exist.")
        }
    }

    fun createSchedule(vet: VetDAO): MutableMap<Int, ScheduleDAO> {
        val newSchedule = mutableMapOf<Int, ScheduleDAO>()
        for (x in 0..29) {
            val newShiftList = createShiftList(vet)
            val value = ScheduleDAO(vet, newShiftList)
            newSchedule[x] = value
        }
        return newSchedule
    }

    // lista com 16 posições correspondendo aos slots que são possiveis marcar uma consulta.
    // posiçao 0: 9:00
    // posiçao 15: 17:00
    // com incrementos de 30 minutos
    // cada shift na lista corresponde a um possivel slot de appointment que e possivel ver se esta
    // disponivel atraves de um booleano
    // temos que assumir que cada posiçao corresponde a um slot de 30 min
    fun createShiftList(vet: VetDAO): MutableList<ShiftDAO> {
        val list = mutableListOf<ShiftDAO>()
        for (x in 0..15) {
            val newShift = ShiftDAO(vet)
            list.add(x, newShift)
        }
        return list
    }

    // como somar de 30 em 30 minutos
//    fun createShift(x: Int) {
//        val year = 2019
//        val month = Month.OCTOBER
//        val start = LocalDateTime.of(year, month, x, 9, 30)
//        val end = start.plusMinutes(30)
//    }

}


