package pt.unl.fct.di.iadi.vetclinic.api

//import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
//import pt.unl.fct.di.iadi.vetclinic.model.VetDAO
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.time.LocalDateTime

data class PetDTO(val id: Long, val name: String, val species: String) {

    constructor(pet: PetDAO) : this(pet.id, pet.name, pet.species)
}

data class AppointmentDTO(val id: Long, var start: LocalDateTime, var end: LocalDateTime, val desc: String) {

    constructor(apt: AppointmentDAO) : this(apt.id, apt.start, apt.end, apt.desc)
}

open class UserDTO(val id: Long, val name: String) {
    constructor(user: UserDAO) : this(user.id, user.name)
}

class ClientDTO(id: Long, name: String) : UserDTO(id, name) {
    constructor(client: ClientDAO) : this(client.id, client.name)
}

//como fazer herança aqui??

class VetDTO(id: Long, name: String, var employeeID: Long) : UserDTO(id, name) {
    constructor(vet: VetDAO) : this(vet.id, vet.name, vet.employeeID)
}

class AdminDTO(id: Long, name: String, var employeeID: Long) : UserDTO(id, name) {
    constructor(admin: AdminDAO) : this(admin.id, admin.name, admin.employeeID)
}