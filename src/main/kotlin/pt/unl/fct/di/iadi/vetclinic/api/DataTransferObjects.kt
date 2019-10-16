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

data class PetDTO(val name:String, val species:String, val owner: ClientDAO, val appointments:MutableList<AppointmentDAO>, val description: String, val notes:MutableList<String>) {

    constructor(pet: PetDAO) : this(pet.name,pet.species, pet.owner, pet.appointments, pet.description, pet.notes)
}

data class AppointmentDTO(val pet: PetDAO, val vet: VetDAO, val start: LocalDateTime,val end: LocalDateTime,val description: String) {

    constructor(appoint: AppointmentDAO) : this( appoint.pet,appoint.vet, appoint.start, appoint.end, appoint.description)
}

data class UserDTO( val name: String, val email :String, val username:String , val password:String, val cellphone:Long, val address:String){
    constructor(user: UserDAO) : this( user.name, user.email ,  user.username , user.password, user.cellphone, user.address)
}

//fazers clientDTO etc, com heranca ??



