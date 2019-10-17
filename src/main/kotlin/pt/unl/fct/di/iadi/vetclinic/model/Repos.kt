package pt.unl.fct.di.iadi.vetclinic.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PetRepository : JpaRepository<PetDAO, Long> {

    fun findByName(name:String): MutableIterable<PetDAO>

    @Query("select p from PetDAO p inner join fetch p.appointments where p.id = :id")
    fun findByIdWithAppointment(id:Long) : Optional<PetDAO>
}


interface AppointmentRepository: JpaRepository<AppointmentDAO, Long>{}

interface UserRepository: JpaRepository<UserDAO, Long>{}

interface VetRepository :UserRepository{}
interface AdminRepository :UserRepository{}