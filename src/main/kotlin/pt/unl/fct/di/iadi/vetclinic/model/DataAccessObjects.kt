/**
Copyright 2019 João Costa Seco

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package pt.unl.fct.di.iadi.vetclinic.model

import net.bytebuddy.asm.Advice
import pt.unl.fct.di.iadi.vetclinic.api.*
import java.time.LocalDateTime
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

    constructor(pet: PetDTO, apts:List<AppointmentDAO>, owner: ClientDAO) : this(pet.id,pet.name,pet.species,pet.frozen, apts, owner)

    fun update(other:PetDAO) {
        this.name = other.name
        this.species = other.species
        this.appointments = other.appointments
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
                        open var photo: String
) {


    constructor(user: UserDTO) : this(user.id, user.name, user.email, user.username, user.password, user.cellphone, user.address, user.photo)
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
                     @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
                var pets:List<PetDAO>,
                     @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL])
                var appointments:List<AppointmentDAO>
) : UserDAO(id,name, email, username, password,cellphone,address, photo) {

    override fun update(other: UserDAO) {
        super.update(other)
    }

    override fun changePassword(password: String) {
        super.changePassword(password)
    }

    constructor(client: ClientDTO, pets: List<PetDAO>, apts:List<AppointmentDAO>) : this(client.id, client.name, client.email, client.username, client.password, client.cellphone, client.address,client.photo, pets, apts)
    constructor() : this(0,"","","","",0,"","", emptyList<PetDAO>(),emptyList<AppointmentDAO>())
    constructor(id: Long,name:String,email: String,username: String,password: String,cellphone: Long,address: String, pets: List<PetDAO>, apts:List<AppointmentDAO>) : this(id, name, email, username, password, cellphone, address,"", pets, apts)
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
        var employeeID: Long,
        var frozen: Boolean,
        @OneToMany(mappedBy = "vet", cascade = [CascadeType.ALL])
        var appointments:List<AppointmentDAO>,
        @OneToMany(mappedBy = "vet", cascade = [CascadeType.ALL])
        var schedules: MutableList<ScheduleDAO>

) : UserDAO(id,name, email, username, password,cellphone,address, photo) {

    constructor(vet: VetDTO, apts:List<AppointmentDAO>, schedules: MutableList<ScheduleDAO>) : this(vet.id, vet.name, vet.email, vet.username, vet.password, vet.cellphone, vet.address,vet.photo ,vet.employeeID, vet.frozen, apts, schedules)
    constructor() : this(0,"","","","",0,"","",0, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>().toMutableList())

    fun updateSchedules(schedules: MutableList<ScheduleDAO>) {
        this.schedules = schedules
    }

    fun updateFrozen(frozen: Boolean) {
        this.frozen = frozen
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
                    var employeeID: Long) : UserDAO(id,name, email, username, password,cellphone,address, photo) {
    constructor(admin: AdminDTO) : this(admin.id, admin.name, admin.email, admin.username, admin.password, admin.cellphone, admin.address,admin.photo, admin.employeeID)
    constructor() : this(0,"","","","",0,"","",0)

    override fun update(other: UserDAO) {
        super.update(other)
    }

    override fun changePassword(password: String) {
        super.changePassword(password)
    }
}


@Entity // each schedule has a month and a list of shifts. number of shifts on list depend on month
data class ScheduleDAO(
        @Id @GeneratedValue val id : Long,
        @ManyToOne var vet: VetDAO,
        val month: Month,
        @OneToMany(mappedBy = "schedule") var shifts: List<ShiftDAO>

) {

    fun updateSlots(shifts: List<ShiftDAO>) {
        this.shifts = shifts
    }

    fun getShiftsList(): List<ShiftDAO> {
        return shifts
    }

    constructor( vet: VetDAO, month: Month, shifts: List<ShiftDAO> ) : this ( 0L, vet, month, shifts )
    constructor( vet: VetDAO, month: Month ) : this ( 0L, vet, month, emptyList() )
}


@Entity // each shift is list of 16 slots of 30 min
data class ShiftDAO(
        @Id @GeneratedValue val id : Long,
        @OneToMany(mappedBy = "shift") var slots: List<SlotDAO>,
        @ManyToOne val schedule: ScheduleDAO
) {

    fun updateSlots(slots: List<SlotDAO>) {
        this.slots = slots
    }

    fun getSlotsList(): List<SlotDAO> {
        return slots
    }

    fun getFreeSlots(): List<SlotDAO> {
        val freeSlots : MutableList<SlotDAO> = mutableListOf()
        for (slot in slots) {
            if (slot.available) {
                freeSlots.add(slot)
            }
        }
        return slots
    }

    constructor( slots: List<SlotDAO>, schedule: ScheduleDAO) : this ( 0L, slots, schedule )
    constructor( schedule: ScheduleDAO ) : this ( 0L, emptyList<SlotDAO>(), schedule )
}


@Entity // each slot has start date and available status
data class SlotDAO(
        @Id @GeneratedValue val id: Long,
        var start: Date,
        var available: Boolean,
        @ManyToOne val shift: ShiftDAO
) {

    constructor(dateTime: Date, shift: ShiftDAO) : this (0L, dateTime, true, shift)

    fun setAvailableFalse() {
        this.available = false
    }

}
