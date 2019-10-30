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

import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.api.ClientDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import pt.unl.fct.di.iadi.vetclinic.api.UserDTO
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
        @ManyToOne          var client: ClientDAO
) {
    constructor() : this(0, Date(),"", PetDAO(), ClientDAO())
    constructor(apt: AppointmentDTO, pet:PetDAO, client: ClientDAO) : this(apt.id, apt.date, apt.desc, pet, client)
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


