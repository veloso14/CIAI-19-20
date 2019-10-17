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

import com.sun.istack.NotNull
import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import java.util.*
import javax.persistence.*

@Entity
data class PetDAO(
        @Id @GeneratedValue val id:Long,
                            var name: String,
                            var species: String,
        @OneToMany(mappedBy = "pet")
                            var appointments:List<AppointmentDAO>
) {
    constructor(pet: PetDTO, apts:List<AppointmentDAO>) : this(pet.id,pet.name,pet.species, apts)

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
        @ManyToOne          var pet:PetDAO
) {

    constructor(apt: AppointmentDTO, pet:PetDAO) : this(apt.id, apt.date, apt.desc, pet)
}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class UserDAO(name: String, email :String,  username:String , password:String, cellphone:Long, address:String){
    @Id
    @GeneratedValue
    val id = -1L

    var name:String = name

    @NotNull
    @Column(name = "u_email" , unique = true)
    var email:String = email

    @NotNull
    @Column(name = "u_username" , unique = true)
    var username:String = username

    @NotNull
    @Column(name = "u_pass")
    var password:String = password

    @NotNull
    @Column(name = "u_cellphone")
    var cellphone:Long = cellphone

    @NotNull
    @Column(name = "u_address")
    var address:String = address
}

@Entity
class ClientDAO(name: String, email :String,  username:String , password:String, cellphone:Long, address:String):UserDAO(name, email ,  username , password, cellphone, address){
    val pets:MutableList<PetDAO> = mutableListOf()
}

// val picture: URI
@Entity
class VetDAO(name: String, email :String,  username:String , password:String, cellphone:Long, address:String, employeeID:Long):UserDAO(name, email ,  username , password, cellphone, address){
    val appointments:MutableList<AppointmentDAO> = mutableListOf()
}


@Entity
class AdminDAO(name: String, email :String,  username:String , password:String, cellphone:Long, address:String,employeeID:Long):UserDAO(name, email ,  username , password, cellphone, address)


