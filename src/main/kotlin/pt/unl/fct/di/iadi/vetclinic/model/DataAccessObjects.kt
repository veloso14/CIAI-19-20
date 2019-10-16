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
 class PetDAO(
         name: String,
         species: String,
         owner: ClientDAO,
         appointments: MutableList<AppointmentDAO>,
         description: String,
         notes: MutableList<String>) {
    constructor(pet: PetDTO) : this(pet.name,pet.species, pet.owner, pet.appointments, pet.description, pet.notes )

    @Id
    @GeneratedValue
    val id:Long = -1


    var name:String = name
    var species:String = species
    var description:String = description

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "owner_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val owner:ClientDAO = owner


    @ElementCollection
    val notes:MutableList<String> = mutableListOf()

    @OneToMany
    val appointments:MutableList<AppointmentDAO> = mutableListOf()


    fun update(other:PetDAO) {
        this.name = other.name

        this.species = other.species
    }


}

@Entity
class  AppointmentDAO(

                       pet: PetDAO,
                        vet:VetDAO,
                       start:LocalDateTime ,
                       end:LocalDateTime ,
                        description: String){

    constructor(appoint: AppointmentDTO) : this( appoint.pet, appoint.vet, appoint.start, appoint.end, appoint.description)

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
    val vet:VetDAO = vet

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
open class UserDAO(name: String, email :String,  username:String , password:String, cellphone:Long, address:String){
    @Id
    @GeneratedValue
    val id = -1

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
class VetDAO(name: String, email :String,  username:String , password:String, cellphone:Long, address:String):UserDAO(name, email ,  username , password, cellphone, address){
    val appointments:MutableList<AppointmentDAO> = mutableListOf()
}


@Entity
class AdminDAO(name: String, email :String,  username:String , password:String, cellphone:Long, address:String):UserDAO(name, email ,  username , password, cellphone, address)


