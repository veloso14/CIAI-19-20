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

import pt.unl.fct.di.iadi.vetclinic.api.*
import java.util.*
import javax.persistence.*

@Entity
data class PetDAO(
        @Id @GeneratedValue val id:Long,
                            var name: String,
                            var species: String,
        @OneToMany(mappedBy = "pet")
                            var appointments:List<AppointmentDAO>,
        @ManyToOne          var owner: ClientDAO
) {
    constructor() : this(0,"","", emptyList(), ClientDAO())

    constructor(pet: PetDTO, apts:List<AppointmentDAO>, owner: ClientDAO) : this(pet.id,pet.name,pet.species, apts, owner)

    fun update(other:PetDAO) {
        this.name = other.name
        this.species = other.species
        this.appointments = other.appointments
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
open class UserDAO(@Id @GeneratedValue  val id: Long,
                                        var name: String,
                                        var email: String,
                   @Column(unique=true) var username: String,
                                        var password: String,
                                        var cellphone: Long,
                                        var address: String
) {
    constructor(user: UserDTO) : this(user.id, user.name, user.email, user.username, user.password, user.cellphone, user.address)
    fun update(other:UserDAO) {
        this.email = other.email
        this.cellphone = other.cellphone
        this.address = other.address
    }

    fun changePassword(password: String){
        this.password = password
    }

}

@Entity
class ClientDAO(id: Long,
                name: String,
                email: String,
                username: String,
                password: String,
                cellphone: Long,
                address: String,
                @OneToMany(mappedBy = "owner")
                var pets:List<PetDAO>,
                @OneToMany(mappedBy = "client")
                var appointments:List<AppointmentDAO>
) : UserDAO(id, name, email, username, password, cellphone, address) {
    constructor(client: ClientDTO, pets: List<PetDAO>, apts:List<AppointmentDAO>) : this(client.id, client.name, client.email, client.username, client.password, client.cellphone, client.address, pets, apts)
    constructor() : this(0,"","","","",0,"", emptyList<PetDAO>(),emptyList<AppointmentDAO>())
}

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
        var frozen: Boolean,
        @OneToMany(mappedBy = "vet")
        var appointments:List<AppointmentDAO>

) : UserDAO(id,name, email ,  username , password, cellphone, address) {
    constructor(vet: VetDTO, apts:List<AppointmentDAO>) : this(vet.id, vet.name, vet.email, vet.username, vet.password, vet.cellphone, vet.address, vet.employeeID, vet.frozen, apts)
    constructor() : this(0,"","","","",0,"",0, false, emptyList<AppointmentDAO>())
    fun updateFrozen(frozen: Boolean) {
        this.frozen = frozen
    }


}

@Entity
class AdminDAO(id: Long,
               name: String,
               email: String,
               username: String,
               password: String,
               cellphone: Long,
               address: String,
               var employeeID: Long) : UserDAO(id, name, email, username, password, cellphone, address) {
    constructor(admin: AdminDTO) : this(admin.id, admin.name, admin.email, admin.username, admin.password, admin.cellphone, admin.address, admin.employeeID)
    constructor() : this(0,"","","","",0,"",0)
}


