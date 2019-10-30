/**
Copyright 2019 João Costa Seco, Eduardo Geraldo

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

package pt.unl.fct.di.iadi.vetclinic.api

import pt.unl.fct.di.iadi.vetclinic.model.*
import java.util.*

/**
 * In this file we define data classes that are used in the controller
 * interface. These classes are defined without any circular references
 * to better control the production of JSON responses.
 * strategy:
 * EntityDTOs contain only the proper attributes, no relations.
 * Relations are added in subsequent DTO classes.
 */

data class PetDTO(val id:Long, val name:String, val species:String, val frozen:Boolean, val ownerID:Long) {
    constructor(pet: PetDAO) : this(pet.id,pet.name,pet.species,pet.frozen, pet.owner.id)
}

data class PetAptsDTO(val pet:PetDTO, val appointments:List<AppointmentDTO>)

data class AppointmentDTO(val id:Long, var date: Date, var desc:String, var petID:Long, var clientID:Long, var vetID:Long) {
    constructor(apt:AppointmentDAO) : this(apt.id, apt.date, apt.desc, apt.pet.id, apt.client.id, apt.vet.id)
}

data class AppointmentPetDTO(val appointment:AppointmentDTO, val pet:PetDTO)

open class UserDTO(val id: Long, val name: String, var email: String,
                   val username: String,
                   var password: String,
                   var cellphone: Long,
                   var address: String) {
    constructor(user: UserDAO) : this(user.id, user.name,user.email,user.username, user.password, user.cellphone, user.address)
}

class ClientDTO(id: Long, name: String, email: String,username: String,password: String,cellphone: Long,address: String) : UserDTO(id, name,email,username,password,cellphone,address) {
    constructor(client: ClientDAO) : this(client.id, client.name, client.email,client.username,client.password, client.cellphone,client.address)
}

data class ClientPetsDTO(val client:ClientDTO, val pets:List<PetDTO>)

class VetDTO(id: Long, name: String, email: String,username: String,password: String,cellphone: Long,address: String,var photo:String, var employeeID: Long, var frozen:Boolean) : UserDTO(id, name,email,username,password,cellphone,address) {
    constructor(vet: VetDAO) : this(vet.id, vet.name,vet.email,vet.username,vet.password, vet.cellphone,vet.address, vet.photo, vet.employeeID, vet.frozen)
}

class AdminDTO(id: Long, name: String, email: String,username: String,password: String,cellphone: Long,address: String,var photo:String, var employeeID: Long) : UserDTO(id, name,email,username,password,cellphone,address) {
    constructor(admin: AdminDAO) : this(admin.id, admin.name,admin.email,admin.username,admin.password, admin.cellphone,admin.address,admin.photo, admin.employeeID)
}
