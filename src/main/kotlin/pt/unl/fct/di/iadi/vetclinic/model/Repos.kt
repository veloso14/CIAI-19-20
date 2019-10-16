package pt.unl.fct.di.iadi.vetclinic.model

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface PetRepository : CrudRepository<PetDAO, Long>{
    @Query("SELECT p FROM PetDAO p WHERE p.id = 0")
    fun petGetZero()

    fun findAllByOwnerId(name: String):MutableIterable<PetDAO>


}

interface AppointRepository  : CrudRepository<AppointmentDAO, Long>{
    fun findAllByPetId(petID:Long):MutableIterable<AppointmentDAO>
    fun findAllByVetId(vetID:Long):MutableIterable<AppointmentDAO>
}

interface UserRepository  : CrudRepository<UserDAO, Long>{


}