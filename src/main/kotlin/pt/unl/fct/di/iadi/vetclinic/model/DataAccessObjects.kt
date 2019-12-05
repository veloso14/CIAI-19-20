package pt.unl.fct.di.iadi.vetclinic.model

import com.fasterxml.jackson.annotation.JsonIgnore
import pt.unl.fct.di.iadi.vetclinic.api.*
import java.time.Month
import java.util.*
import javax.persistence.*

@Entity
data class PetDAO(
        @Id @GeneratedValue val id:Long,
        var name: String,
        var species: String,
        var frozen: Boolean,
        @OneToMany(mappedBy = "pet", cascade = [CascadeType.ALL])
        var appointments:List<AppointmentDAO>,
        @ManyToOne          var owner: ClientDAO
) {
    constructor() : this(0,"","",false, emptyList(), ClientDAO())

    constructor(pet: PetDTO, apts:List<AppointmentDAO>, owner: ClientDAO) : this(pet.id,pet.name,pet.species,false, apts, owner)
    constructor(pet: PetUpdateDTO) : this(pet.id,pet.name,pet.species,false, emptyList(), ClientDAO())

    fun update(other:PetDAO) {
        this.name = other.name
        this.species = other.species
    }

    fun frozenPet() {
        this.frozen = true
    }
}

@Entity
data class AppointmentDAO(
        @Id @GeneratedValue val id:Long,
        var date: Date,
        var desc:String,
        @ManyToOne          var pet:PetDAO,
        @ManyToOne          var client: ClientDAO,
        @ManyToOne          var vet: VetDAO
) {
    constructor() : this(0, Date(),"", PetDAO(), ClientDAO(), VetDAO())
    constructor(apt: AppointmentDTO, pet:PetDAO, client: ClientDAO, vet: VetDAO) : this(apt.id, apt.date, apt.desc, pet, client, vet)

    // se desc for diferente de "" entao o appointment esta completo
    fun complete(desc: String){
        this.desc = desc
    }
}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class UserDAO( @Id @GeneratedValue open val id: Long,
                        open val name: String,
                        open  var email: String,
                        open var username: String,
                        open var password: String,
                        open var cellphone: Long,
                        open var address: String,
                        open var photo: String,
                        open var role: String
) {


    constructor(user: UserDTO) : this(user.id, user.name, user.email, user.username, user.password, user.cellphone, user.address, user.photo, "")
    open fun update(other:UserDAO) {

        this.email = other.email
        this.cellphone = other.cellphone
        this.address = other.address


    }



    open fun changePassword(password: String) {
        this.password = password
    }



}

@Entity
data class ClientDAO(override val id: Long,
                     override val name: String,
                     override  var email: String,
                     override  var username: String,
                     override  var password: String,
                     override  var cellphone: Long,
                     override  var address: String,
                     override  var photo:String,
                     override  var role:String,
                     @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
                     var pets:List<PetDAO>,
                     @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL])
                     var appointments:List<AppointmentDAO>
) : UserDAO(id,name, email, username, password,cellphone,address, photo, role) {

    override fun update(other: UserDAO) {
        super.update(other)
    }

    override fun changePassword(password: String) {
        super.changePassword(password)
    }

    constructor(client: UserUpdateDTO) : this(client.id,"",client.email,"","",client.cellphone,client.address,"","CLIENT", emptyList<PetDAO>(),emptyList<AppointmentDAO>())
    constructor(client: ClientDTO, pets: List<PetDAO>, apts:List<AppointmentDAO>) : this(client.id, client.name, client.email, client.username, client.password, client.cellphone, client.address,client.photo,"CLIENT", pets, apts)
    constructor() : this(0,"","","","",0,"","","CLIENT", emptyList<PetDAO>(),emptyList<AppointmentDAO>())
    constructor(id: Long,name:String,email: String,username: String,password: String,cellphone: Long,address: String, pets: List<PetDAO>, apts:List<AppointmentDAO>) : this(id, name, email, username, password, cellphone, address,"","CLIENT", pets, apts)
}

@Entity
data class VetDAO(
        override val id: Long,
        override  val name: String,
        override  var email: String,
        override  var username: String,
        override  var password: String,
        override  var cellphone: Long,
        override  var address: String,
        override var photo:String,
        override var role:String,
        var employeeID: Long,
        var frozen: Boolean,
        @OneToMany(mappedBy = "vet", cascade = [CascadeType.ALL])
        var appointments:List<AppointmentDAO>,
        @OneToMany(mappedBy = "vet", cascade = [CascadeType.ALL])
        var schedules:List<ScheduleDAO>

) : UserDAO(id,name, email, username, password,cellphone,address, photo, role) {
    constructor(vet: VetDTO, apts:List<AppointmentDAO>, schedules:List<ScheduleDAO>) : this(vet.id, vet.name, vet.email, vet.username, vet.password, vet.cellphone, vet.address,vet.photo,"VET" ,vet.employeeID, false, apts, schedules)
    constructor() : this(0,"","","","",0,"","","VET",0, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())
    constructor(id: Long,name:String,email: String,username: String,password: String,cellphone: Long,address: String, photo:String, employeeID: Long,frozen: Boolean, apts:List<AppointmentDAO>, schedules:List<ScheduleDAO>) : this(id, name, email, username, password, cellphone, address,photo,"VET",employeeID,frozen, apts, schedules)
    constructor(vet: UserUpdateDTO) : this(vet.id,"",vet.email,"","",vet.cellphone,vet.address,"","VET",0,false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())

    fun updateFrozen(frozen: Boolean) {
        this.frozen = frozen
    }

    fun addSchedule(schedule: ScheduleDAO) {
        val tempSchedules = this.schedules.toMutableList()
        tempSchedules.add(schedule)
        this.schedules = tempSchedules.toList()
    }

    //secalhar nao e preciso declarar as funcoes
    override fun update(other: UserDAO) {
        super.update(other)
    }

    override fun changePassword(password: String) {
        super.changePassword(password)
    }


}

@Entity
data class AdminDAO( override val id: Long,
                     override  val name: String,
                     override  var email: String,
                     override  var username: String,
                     override  var password: String,
                     override  var cellphone: Long,
                     override  var address: String,
                     override var photo:String,
                     override var role:String,
                     var employeeID: Long) : UserDAO(id,name, email, username, password,cellphone,address, photo, role) {
    constructor(admin: AdminDTO) : this(admin.id, admin.name, admin.email, admin.username, admin.password, admin.cellphone, admin.address,admin.photo, "ADMIN",admin.employeeID)
    constructor() : this(0,"","","","",0,"","","ADMIN",0)
    constructor(id: Long,name:String,email: String,username: String,password: String,cellphone: Long,address: String, photo:String, employeeID: Long) : this(id, name, email, username, password, cellphone, address,photo,"ADMIN",employeeID)
    constructor(admin: UserUpdateDTO) : this(admin.id,"",admin.email,"","",admin.cellphone,admin.address,"","ADMIN",0)

    override fun update(other: UserDAO) {
        super.update(other)
    }

    override fun changePassword(password: String) {
        super.changePassword(password)
    }
}

@Entity // each schedule has a month and a list of shifts. number of shifts on list depend on month
data class ScheduleDAO(
        @Id @GeneratedValue val id: Long,

        @ManyToOne(cascade = [CascadeType.ALL]) var vet: VetDAO,
        val month: Month,
        @OneToMany(mappedBy = "schedule", cascade = [CascadeType.ALL]) var shifts: List<ShiftDAO>

) {

    fun updateShifts(shifts: List<ShiftDAO>) {
        this.shifts = shifts
    }

    constructor(vet: VetDAO, month: Month, shifts: List<ShiftDAO>) : this(0L, vet, month, shifts)
    constructor(vet: VetDAO, month: Month) : this(0L, vet, month, emptyList())
    constructor(month: Month) : this(0L, VetDAO(), month, emptyList())
}


@Entity // each shift is list of 16 slots of 30 min
data class ShiftDAO(
        @Id @GeneratedValue val id: Long,
        @OneToMany(mappedBy = "shift", cascade = [CascadeType.ALL]) var slots: List<SlotDAO>,
        @JsonIgnore
        @ManyToOne(cascade = [CascadeType.ALL]) val schedule: ScheduleDAO
) {


    fun getFreeSlots(): List<SlotDAO> {
        val freeSlots: MutableList<SlotDAO> = mutableListOf()
        for (slot in slots) {
            if (slot.available) {
                freeSlots.add(slot)
            }
        }
        return slots
    }

    fun updateSlots(slots: List<SlotDAO>) {
        this.slots = slots
    }

    constructor(slots: List<SlotDAO>, schedule: ScheduleDAO) : this(0L, slots, schedule)
    constructor(schedule: ScheduleDAO) : this(0L, emptyList<SlotDAO>(), schedule)

}


@Entity// each slot has start date and available status
data class SlotDAO(
        @Id @GeneratedValue val id: Long,
        var start: Long,
        var available: Boolean,
        @JsonIgnore
        @ManyToOne(cascade = [CascadeType.ALL]) val shift: ShiftDAO
) {

    constructor(date: Date, shift: ShiftDAO) : this(0L, date.time, true, shift)

    fun setAvailableFalse() {
        this.available = false
    }

}



@Entity
data class UserSecurityDAO(
        @Id
        val username: String = "",
        var password: String = "")
{
    constructor(user:UserPasswordDTO) : this(user.username, user.password)
}

