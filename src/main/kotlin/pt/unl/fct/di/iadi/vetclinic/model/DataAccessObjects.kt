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
                        open var address: String
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
data class ClientDAO(override val id: Long,
                     override val name: String,
                     override  var email: String,
                     override  var username: String,
                     override  var password: String,
                     override  var cellphone: Long,
                     override  var address: String,
                     @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
                var pets:List<PetDAO>,
                     @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL])
                var appointments:List<AppointmentDAO>
) : UserDAO(id,name, email, username, password,cellphone,address) {
    constructor(client: ClientDTO, pets: List<PetDAO>, apts:List<AppointmentDAO>) : this(client.id, client.name, client.email, client.username, client.password, client.cellphone, client.address, pets, apts)
    constructor() : this(0,"","","","",0,"", emptyList<PetDAO>(),emptyList<AppointmentDAO>())
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
        var photo:String,
        var employeeID: Long,
        var frozen: Boolean,
        @OneToMany(mappedBy = "vet", cascade = [CascadeType.ALL])
        var appointments:List<AppointmentDAO>

) : UserDAO(id,name, email, username, password,cellphone,address) {
    constructor(vet: VetDTO, apts:List<AppointmentDAO>) : this(vet.id, vet.name, vet.email, vet.username, vet.password, vet.cellphone, vet.address,vet.photo ,vet.employeeID, vet.frozen, apts)
    constructor() : this(0,"","","","",0,"","",0, false, emptyList<AppointmentDAO>())
    fun updateFrozen(frozen: Boolean) {
        this.frozen = frozen
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
                    var photo:String,
                    var employeeID: Long) : UserDAO(id,name, email, username, password,cellphone,address) {
    constructor(admin: AdminDTO) : this(admin.id, admin.name, admin.email, admin.username, admin.password, admin.cellphone, admin.address,admin.photo, admin.employeeID)
    constructor() : this(0,"","","","",0,"","",0)
}


