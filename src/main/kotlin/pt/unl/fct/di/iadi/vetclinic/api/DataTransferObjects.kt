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

import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.model.VetDAO
import java.util.*

data class PetDTO(val id:Long, val name:String, val species:String) {

    constructor(pet: PetDAO) : this(pet.id,pet.name,pet.species)
}

data class AppointmentDTO(val id:Long, val date:Date, val desc:String) {

    constructor(apt: AppointmentDAO) : this(apt.id, apt.date, apt.desc)
}

open class UserDTO(val id:Long, val name:String){
    constructor(user: UserDAO) : this(user.id, user.name)
}
//como fazer herança aqui??
/*
class VetDTO(val id:Long, val name:String, val employeeID:Long):UserDAO(id, name){
    constructor(vet: VetDAO) : this(vet.id, vet.name, vet.employeeID )
}*/