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

import ch.qos.logback.core.net.server.Client
import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.api.ClientDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class PetDAO(
        @Id @GeneratedValue val id:Long,
                            var name: String,
                            var species: String,
        @OneToMany(mappedBy = "pet")
                            var appointments:List<AppointmentDAO>,
        @ElementCollection
                             var notes:List<String>,
        @ManyToOne()    var owner: ClientDAO
) {
    constructor() : this(0,"","", emptyList(), emptyList(), ClientDAO())

    constructor(pet: PetDTO, apts:List<AppointmentDAO>,notes:List<String>, owner:ClientDAO) : this(pet.id,pet.name,pet.species, apts, notes, owner)

    fun update(other:PetDAO) {
        this.name = other.name
        this.species = other.species
        this.appointments = other.appointments
        this.notes = other.notes
    }
}

@Entity
data class AppointmentDAO(
        @Id @GeneratedValue val id:Long,
                            var start: LocalDateTime,
                            var end:LocalDateTime,
                            var desc:String,
                            var complete:Boolean,
        @ManyToOne          var pet:PetDAO,
        @ManyToOne          var client:ClientDAO,
        @ManyToOne          var vet:VetDAO


) {
    constructor() : this(0, LocalDateTime.MIN, LocalDateTime.MAX,"",false, PetDAO(), ClientDAO(), VetDAO())
    constructor(apt: AppointmentDTO, pet:PetDAO, client: ClientDAO, vet:VetDAO) : this(apt.id, apt.start, apt.end, apt.desc, apt.complete, pet, client, vet)
    fun updateComplete(complete: Boolean) {
        this.complete = complete
    }
}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class BackListDAO(@Id @GeneratedValue val id: Long,
                       @NotNull
                       @Column(name = "JWT", unique = true)
                       var key: String) {
    constructor(lista: BackListDAO) : this(lista.id , lista.key)


}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class UserDAO(@Id @GeneratedValue val id: Long,
                   var name: String,
                   var email: String,
                   var username: String,
                   var password: String,
                   var cellphone: Long,
                   var address: String) {
    constructor(user: UserDAO) : this(user.id, user.name, user.email, user.username, user.password, user.cellphone, user.address)


}

@Entity
class ClientDAO(id: Long,
                name: String,
                email: String,
                username: String,
                password: String,
                cellphone: Long,
                address: String,
                @OneToMany(mappedBy = "owner", cascade = arrayOf(CascadeType.ALL))
                var pets:List<PetDAO>,
                @OneToMany(mappedBy = "client")
                var appointments:List<AppointmentDAO>
                ) : UserDAO(id, name, email, username, password, cellphone, address) {
    constructor(client: ClientDTO, pets:List<PetDAO>, apts:List<AppointmentDAO>) : this(client.id, client.name, client.email, client.username, client.password, client.cellphone, client.address, pets, apts)
    //val pets:MutableList<PetDAO> = mutableListOf()
    constructor() : this(0,"","","","",0,"", emptyList(), emptyList())
}

// val picture: URI
@Entity
class VetDAO(
        id: Long,
        name: String,
        email: String,
        username: String,
        password: String,
        cellphone: Long,
        address: String,
        var employeeID: Long,
        @OneToMany(mappedBy = "vet")
        var appointments: List<AppointmentDAO>,
        var frozen: Boolean
) : UserDAO(id,name, email ,  username , password, cellphone, address) {
    constructor(vet: VetDAO) : this(vet.id, vet.name, vet.email, vet.username, vet.password, vet.cellphone, vet.address, vet.employeeID,  vet.appointments, vet.frozen)
    constructor() : this(0,"","","","",0,"",0, emptyList(), false)
    fun updateFrozen(frozen: Boolean) {
        this.frozen = frozen
    }

}
@Entity
class AdminDAO(id: Long, name: String, email: String, username: String, password: String, cellphone: Long, address: String, var employeeID: Long) : UserDAO(id, name, email, username, password, cellphone, address) {
    constructor(admin: AdminDAO) : this(admin.id, admin.name, admin.email, admin.username, admin.password, admin.cellphone, admin.address, admin.employeeID)
}

@Entity
data class ShiftDAO(
        @Id @GeneratedValue val id: Long,
        //val start: LocalDateTime,
        //val end: LocalDateTime,
        @ManyToOne val vet: VetDAO,
        var available: Boolean
) {

    constructor(shift: ShiftDAO, vet: VetDAO) : this(shift.id, /*shift.start, shift.end,*/ vet, true)

    constructor(vet: VetDAO) : this(0, /* LocalDateTime.MIN,LocalDateTime.MAX,*/ vet, true)

    fun setAvailableFalse() {
        this.available = false
    }
}

data class VetScheduleDAO(
        val vet: VetDAO,
        @OneToMany val shifts: MutableMap<Int, MutableList<ShiftDAO>>
) {
    fun getScheduleByDay(day: Int): List<ShiftDAO>? {
        return shifts[day]
    }
}

