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
import java.time.LocalDateTime
import java.util.*

/**
 * In this file we define data classes that are used in the controller
 * interface. These classes are defined without any circular references
 * to better control the production of JSON responses.
 * strategy:
 * EntityDTOs contain only the proper attributes, no relations.
 * Relations are added in subsequent DTO classes.
 */

data class PetDTO(val id:Long, val name:String, val species:String) {
    constructor(pet: PetDAO) : this(pet.id,pet.name,pet.species)
}

data class PetAptsDTO(val pet:PetDTO, val appointments:List<AppointmentDTO>)
data class PetOwnerDTO(val pet:PetDTO, val owner:ClientDTO)

data class AppointmentDTO(val id:Long, var start:LocalDateTime, var end:LocalDateTime , var desc:String, var complete:Boolean) {
    constructor(apt:AppointmentDAO) : this(apt.id, apt.start, apt.end, apt.desc, apt.complete)
}


data class AppointmentPetDTO(val appointment:AppointmentDTO, val pet:PetDTO)
data class AppointmentClientDTO(val appointment:AppointmentDTO, val client: ClientDAO)




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


class VetDTO(id: Long, name: String, email: String,username: String,password: String,cellphone: Long,address: String, var employeeID: Long) : UserDTO(id, name,email,username,password,cellphone,address) {
    constructor(vet: VetDAO) : this(vet.id, vet.name,vet.email,vet.username,vet.password, vet.cellphone,vet.address, vet.employeeID)
}

data class VetAptsDTO(val vet:VetDTO, val appointments:List<AppointmentDTO>)

class AdminDTO(id: Long, name: String, email: String,username: String,password: String,cellphone: Long,address: String, var employeeID: Long) : UserDTO(id, name,email,username,password,cellphone,address) {
    constructor(admin: AdminDAO) : this(admin.id, admin.name,admin.email,admin.username,admin.password, admin.cellphone,admin.address, admin.employeeID)
}

data class VetScheduleDTO(val id: Long, val vet: VetDAO){
    constructor(schedule:VetScheduleDAO):this(schedule.id,schedule.vet)
}
