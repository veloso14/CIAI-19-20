package pt.unl.fct.di.iadi.vetclinic.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface PetRepository : JpaRepository<PetDAO, Long> {

    fun findByName(name: String): MutableIterable<PetDAO>

    @Query("select p from PetDAO p inner join fetch p.appointments where p.id = :id")
    fun findByIdWithAppointment(id: Long): Optional<PetDAO>
}


interface AppointmentRepository : JpaRepository<AppointmentDAO, Long> {
    //fun findAllByPetId(petID: Long): MutableIterable<AppointmentDAO>
}

interface UserRepository : JpaRepository<UserDAO, String> {

}

interface VetRepository : JpaRepository<UserDAO, String> {}
interface AdminRepository : JpaRepository<UserDAO, String> {}
interface ClientRepository : JpaRepository<UserDAO, String> {}