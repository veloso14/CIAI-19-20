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

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import java.net.URI
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class PetDAO(
        @Id @GeneratedValue val id:Long,
        var name: String,
        var species: String
) {
    constructor(pet: PetDTO) : this(pet.id,pet.name,pet.species)

    fun update(other:PetDAO) {
        this.name = other.name

        this.species = other.species
    }
}

@Entity
class  AppointmentDAO(
                        id: Long,
                       pet: PetDAO,
                        employee:VetDAO,
                       start:LocalDateTime ,
                       end:LocalDateTime ,
                        description: String){

    @Id
    @GeneratedValue
    val id:Long = -1
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "pet_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val pet:PetDAO = pet

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "vet_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val vet:VetDAO = employee

    var start:LocalDateTime = start
    var end:LocalDateTime = end
    var description:String = description

    /*
    fun update(other:AppointmentDAO) {
        this.id = other.id
    this.pet: PetDAO,
    this.employee:VetDAO,
    this.start:LocalDateTime ,
    this.end:LocalDateTime ,
    this.description: String
}*/
   // constructor(appointment: AppointmentDTO) : this(appointment.id ,appointment.pet,appointment.start, appointment.end , appointment.description )

}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class UserDAO(name: String, email :String){
    @Id
    @GeneratedValue
    val id = -1

    var name:String = name

    @NotNull
    @Column(name = "u_email" , unique = true)
    var email:String = email
}

@Entity
class ClientDAO(name: String, email :String ):UserDAO(name, email){

}

@Entity
class VetDAO( name:String ,  username:String , password:String , email:String , cellphone:Long , adress:String, val picture: URI): UserDAO( name , username)


@Entity
class AdminDAO(name:String, username:String, password:String, email:String, cellphone:Long, adress:String, picture: URI): UserDAO( name , username)


